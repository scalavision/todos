package todos.dom

import org.scalajs.dom.document
import com.raquo.laminar.api.L

/** Utilities for anything related to the Javascript Dom API
  */

val body = document.body
val html = document.documentElement

inline def renderOnWindowContentLoaded(
    container: => org.scalajs.dom.Element,
    rootNode: => com.raquo.laminar.nodes.ReactiveElement.Base
): Unit =
  L.windowEvents.onLoad.foreach { _ =>
    new L.RootNode(container, rootNode)
  }(L.unsafeWindowOwner)

inline def app() = document.getElementById("app")

inline def init() =
  html.classList.add(s"bg-skin-bkg-900")
  html.classList.add(s"text-skin-text-100")
