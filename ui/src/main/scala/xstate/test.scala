package xstate.test

import scala.scalajs.js
import xstate.facade.*

/** Playground for learning XState
  */

class FinalState extends js.Object:
  val `type`: String = "final"

class PromiseState extends js.Object:
  val pending = "pending"
  val resolved = FinalState()
  val rejected = FinalState()

class PromiseMachine extends js.Object:
  val id = "promise"
  val initial = "pending"
  val state = PromiseState()

/** Example from Javascript Marathon, XState fundamentals
  */

def promiseMachine() =
  createMachine(new PromiseMachine())
