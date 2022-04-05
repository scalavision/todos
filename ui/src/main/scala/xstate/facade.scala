package xstate.facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import xstate.types.*

@js.native
@JSImport("xstate", "createMachine")
def createMachine(config: MachineConfig): js.Any = js.native
