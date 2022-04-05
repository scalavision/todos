package xstate

import scala.deriving.Mirror
import scala.compiletime.{constValue, erasedValue, summonInline}

// references:
// https://github.com/scalavision/native-converter
// https://august.nagro.us/scalajs-native-converter.html
// https://blog.philipp-martini.de/blog/magic-mirror-scala3/
// https://www.scala-lang.org/2021/02/26/tuples-bring-generic-programming-to-scala-3.html
// https://eed3si9n.com/intro-to-scala-3-macros/
// https://softwaremill.com/scala-3-macros-tips-and-tricks/
// https://blog.softwaremill.com/starting-with-scala-3-macros-a-short-tutorial-88e9d2b2584c
// https://blog.oyanglul.us/scala/dotty/en/type-classes
// https://www.47deg.com/blog/scala-3-typeclasses/
trait GenericDeriver[TC[_]]:
  case class CaseClassElement[A, B](
      label: String,
      typeclass: TC[B],
      getValue: A => B,
      idx: Int
  )
  case class CaseClassType[A](
      label: String,
      elements: List[CaseClassElement[A, _]],
      fromElements: List[Any] => A
  )

  case class SealedElement[A, B](
      label: String,
      typeclass: TC[B],
      idx: Int,
      cast: A => B
  )
  case class SealedType[A](
      label: String,
      elements: List[SealedElement[A, _]],
      getElement: A => SealedElement[A, _]
  )

  inline def getInstances[A <: Tuple]: List[TC[Any]] =
    inline erasedValue[A] match {
      case _: EmptyTuple => Nil
      case _: (t *: ts) =>
        summonInline[TC[t]].asInstanceOf[TC[Any]] :: getInstances[ts]
    }

  inline def getElemLabels[A <: Tuple]: List[String] =
    inline erasedValue[A] match {
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => constValue[t].toString :: getElemLabels[ts]
    }

  def deriveCaseClass[A](caseClassType: CaseClassType[A]): TC[A]

  def deriveSealed[A](sealedType: SealedType[A]): TC[A]

  inline given derived[A](using m: Mirror.Of[A]): TC[A] = {
    val label = constValue[m.MirroredLabel]
    val elemInstances = getInstances[m.MirroredElemTypes]
    val elemLabels = getElemLabels[m.MirroredElemLabels]

    inline m match {
      case s: Mirror.SumOf[A] =>
        val elements = elemInstances.zip(elemLabels).zipWithIndex.map {
          case ((inst, lbl), idx) =>
            SealedElement[A, Any](
              lbl,
              inst.asInstanceOf[TC[Any]],
              idx,
              identity
            )
        }
        val getElement = (a: A) => elements(s.ordinal(a))
        deriveSealed(SealedType[A](label, elements, getElement))

      case p: Mirror.ProductOf[A] =>
        val caseClassElements =
          elemInstances
            .zip(elemLabels)
            .zipWithIndex
            .map { case ((inst, lbl), idx) =>
              CaseClassElement[A, Any](
                lbl,
                inst.asInstanceOf[TC[Any]],
                (x: Any) => x.asInstanceOf[Product].productElement(idx),
                idx
              )
            }
        val fromElements: List[Any] => A = { elements =>
          val product: Product = new Product {
            override def productArity: Int = caseClassElements.size

            override def productElement(n: Int): Any = elements(n)

            override def canEqual(that: Any): Boolean = false
          }
          p.fromProduct(product)
        }
        deriveCaseClass(
          CaseClassType[A](label, caseClassElements, fromElements)
        )
    }
  }
