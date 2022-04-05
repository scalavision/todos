package xstate

// https://eed3si9n.com/intro-to-scala-3-macros/
import scala.scalajs.js
import scala.deriving.Mirror

trait Codec[A]:
  self =>
  def toJs(a: A): js.Any
  def fromJs(dict: js.Any): Option[A]

  extension (a: A) def asJs: js.Any = self.toJs(a)

object Codec extends GenericDeriver[Codec]:

  def apply[A](using instance: Codec[A]): Codec[A] = instance

  inline def to[A](
      toJsFn: A => js.Any,
      fromJSFn: js.Any => Option[A]
  ): Codec[A] = new Codec[A]:
    def toJs(a: A): js.Any = toJsFn(a)
    def fromJs(jsValue: js.Any) = fromJSFn(jsValue)

  override def deriveCaseClass[A](
      caseClassType: CaseClassType[A]
  ): Codec[A] = new Codec[A] {
    def toJs(a: A): js.Any =
      val props =
        caseClassType.elements.foldLeft(Seq.empty[(String, js.Any)]) {
          (acc, p) =>
            (p.label, p.typeclass.toJs(p.getValue(a))) +: acc
        }
      val allProps =
        ("type" -> caseClassType.label) +: props
      js.Dictionary((("type" -> caseClassType.label) +: props): _*)
    def fromJs(jsValue: js.Any) =
      val dict = jsValue.asInstanceOf[js.Dictionary[js.Any]]
//      if dict("type").toString != caseClassType.label then None
//      else
      val elems = caseClassType.elements
        .map { el =>
          el.typeclass.fromJs(dict(el.label))
        }

      val productElems = elems.collect { case Some(value) => value }
      Some(caseClassType.fromElements(productElems))

  }
  override def deriveSealed[A](
      sealedType: SealedType[A]
  ): Codec[A] = new Codec[A]:

    def toJs(a: A): js.Any =
      val elem = sealedType.getElement(a)
      elem.typeclass.toJs(elem.cast(a))

    def fromJs(dict: js.Any) =
      val elems = sealedType.elements
      elems
        .foldLeft(List.empty[Option[A]]) { (acc, a) =>
          a.typeclass.fromJs(dict).asInstanceOf[Option[A]] +: acc
        }
        .filter(_.isDefined)
        .head

  given Codec[Int] = to(a => a, aa => Some(aa.toString.toInt))
  given Codec[String] =
    to(a => a, aa => Some(aa.toString))

  import js.JSConverters._

  given [A: Codec]: Codec[Vector[A]] = to(
    v => v.map(a => a.asJs).toJSArray,
    jsArray => {
      val array: js.Array[js.Any] = jsArray.asInstanceOf[js.Array[js.Any]]
      val empty: Vector[A] = Vector()
      Some(
        array
          .foldLeft(empty)((acc, ajs) => {
            ajs.toA.fold(acc)(a => a +: acc)
          })
          .reverse
      )
    }
  )

  given [A: Codec]: Codec[Seq[A]] = to(
    v => (v.map(a => a.asJs).toJSArray).asInstanceOf[js.Any],
    jsArray => {
      val array: js.Array[js.Any] = jsArray.asInstanceOf[js.Array[js.Any]]
      val empty: Seq[A] = Seq()
      Some(
        array
          .foldLeft(empty)((acc, ajs) => {
            ajs.toA.fold(acc)(a => a +: acc)
          })
          .reverse
      )
    }
  )

  given [A: Codec]: Codec[js.Array[A]] = to(
    v => v.map(a => a.asJs),
    jsArray => {
      Some(
        jsArray
          .asInstanceOf[js.Array[js.Any]]
          .foldLeft(js.Array[A]())((acc, ajs) => {
            ajs.toA.fold(acc)(a => a +: acc)
          })
          .collect { case Some(v) => v }
          .reverse
          .asInstanceOf[js.Array[A]]
      )
    }
  )

  given [A: Codec]: Codec[Option[A]] = to(
    v => v.fold("null".asInstanceOf[js.Any])(a => a.asJs),
    jsOpt => {
      if (jsOpt.toString == "undefined" || jsOpt.toString == "null") None
      else Some(jsOpt.toA)
    }
  )

  given Codec[Boolean] =
    to(
      b => if b == false then "false" else "true",
      s => if s.toString() == "false" then Some(false) else Some(true)
    )

  def toJs[A](a: A)(using codec: Codec[A]) =
    codec.toJs(a)

  def fromJS[A](a: js.Any)(using codec: Codec[A]) =
    codec.fromJs(a)

extension (dict: js.Any) def toA[A](using codec: Codec[A]) = codec.fromJs(dict)
