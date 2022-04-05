package todos

import com.raquo.laminar.api.L
object Main:

  def main(args: Array[String]): Unit =
    todos.dom.init()
    todos.dom.renderOnWindowContentLoaded(
      todos.dom.app(),
      todos.Page()
    )
