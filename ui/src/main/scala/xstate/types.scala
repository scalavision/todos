package xstate.types

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*
import scala.scalajs.js.annotation.JSImport

@js.native
trait MachineSchema extends js.Object {
  val context: js.Any
  val events: js.Any
  val actions: js.Any
}

@js.native
trait StateNodeConfig extends js.Object {
  val id: String
  val states: js.Any
  val initial: js.Any
}

@js.native
trait MachineConfig extends StateNodeConfig {
  val context: js.Any
  val version: Option[String]
}
