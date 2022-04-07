package xstate.types

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.annotation.JSName

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

@js.native
trait Clock extends js.Object {
  // fn is a multiarg function, not sure how to model that in ScalaJS
  def setTimeout(fn: js.Function1[js.Any, Unit], ms: Int): js.Any = js.native
  def clearTimeout(id: js.Any): Unit = js.native
}

@js.native
trait InterpreterOptions extends js.Object {
  val id: Option[String] = js.native
  val execute: Option[Boolean] = js.native
  val deferEvents: Option[Boolean] = js.native
  val clock: Option[Clock] = js.native
  val parent: Option[Interpreter] = js.native
  val devTools: Option[Boolean | js.Object] = js.native
}

@js.native
trait DefaultOptions extends js.Object {
  val execute: Boolean = js.native
  val deferEvents: Boolean = js.native
  val clock: Clock = js.native
  val devTools: Boolean = js.native
}

type NotStarted = 0
type Running = 1
type Stopped = 2

type InterpreterStatus = NotStarted | Running | Stopped

trait EventObject extends js.Object {
  val `type`: String
}

@js.native
trait ActorRef extends js.Object {
  val id: String = js.native
  val stop: Option[Unit => Unit] = js.native
  val toJSON: Option[Unit => js.Any] = js.native
  val send: EventObject = js.native
}

@js.native
trait BaseActionObject extends js.Object {
  val `type`: String = js.native
}

/* Probably don't need these
@js.native
trait ActionMeta extends js.Object {

}

@js.native
trait ActionFunction extends js.Object {
  def bivarianceHack(
    context: js.Any,
    event: EventObject,
    meta: ActionMeta
  ): Unit = js.native
}

 */

@js.native
trait ActionObject extends BaseActionObject {
  // TODO: This is probably several different kind of functions
  val exec: Option[js.Any] = js.native
}

@js.native
trait ActivityDefinition extends ActionObject {
  val id: String = js.native
}

@js.native
trait ActivityMap extends ActivityDefinition

@js.native
trait StateSchema extends js.Object {
  val meta: Option[js.Any] = js.native
  val context: Option[js.Any] = js.native
  val states: js.Dictionary[StateSchema] = js.native
}

@js.native
trait StateNode extends js.Object {
  val key: String = js.native
  val id: String = js.native
  val version: Option[String] = js.native
  val `type`: "atomic" | "compund" | "parallel" | "final" | "history" =
    js.native
  val path: js.Array[String] = js.native
  // val states:

}

@js.native
trait StateMachine extends js.Object {
  val id: String = js.native
  val states: StateNode = js.native
  def withConfig(
      options: js.Any,
      config: js.Any
  ): StateMachine = js.native

}

trait Observer[T] extends js.Object {
  def next(value: T): Unit
  def error(err: js.Any): Unit
  def complete: () => Unit
}

trait Subscription extends js.Object {
  def unsubscribe(): Unit
}

trait Subscribable[T] extends js.Object {
  def subscribe(observer: Observer[T]): Subscription
}

type StateValue = String | js.Object
trait StateConfig[TContext] extends js.Object {
  val value: StateValue
  val context: TContext
  val actions: js.UndefOr[js.Any]
  val activities: js.UndefOr[ActivityMap]
  val machine: js.UndefOr[StateMachine]
}
@js.native
trait State extends js.Object {
  val actions: js.Array[ActionObject] = js.native
  val activities: ActivityMap = js.native
  val events: js.Array[EventObject] = js.native
  val event: EventObject = js.native
  val changed: Option[Boolean] = js.native
  val done: Option[Boolean] = js.native
  val configuration: js.Array[StateNode] = js.native
  val children: js.Map[String | Int, ActorRef] = js.native
  val tags: js.Set[String] = js.native
  val machine: Option[StateMachine] = js.native
  def from(
      stateValue: State | StateValue
  ): State = js.native
  def create[TContext](
      config: StateConfig[TContext]
  ): State = js.native

}

trait StateListener extends js.Function2[State, EventObject, Unit] {
  def apply(
      state: State,
      event: EventObject
  ): Unit
}

@js.native
trait Interpreter extends ActorRef {
  val defaultOptions: DefaultOptions = js.native
  val clock: Clock = js.native
  val options: InterpreterOptions = js.native
  val initialized: Boolean = js.native
  val status: InterpreterStatus = js.native
  val parent: Option[Interpreter] = js.native
  val sessionId: String = js.native
  val children: js.Map[String | Int, ActorRef] = js.native
  def initialState: State = js.native
  def state(): State = js.native
  def interpret(stateMachien: js.Any): Interpreter = js.native
  def execute(state: State, actionsConfig: Option[js.Any] = None): Unit =
    js.native
  def onTransition(listener: StateListener): Interpreter = js.native
}
