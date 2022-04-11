package xstate.facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import xstate.types.*

/** finite state machine consists of:
  *   - events
  *   - state
  *   - effects / side effects running when:
  *     - an event occurs
  *     - a state is changed, just before, just after
  */

@js.native
@JSImport("xstate", "createMachine")
def createMachine(config: js.Any): js.Any = js.native

@js.native
@JSImport("xstate", "interpret")
def interpret(machine: js.Any): js.Any = js.native
