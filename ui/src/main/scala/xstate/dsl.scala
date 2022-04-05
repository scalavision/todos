package xstate.dsl

import scala.scalajs.js
import xstate.facade

/*
Advanced example:
http://realfiction.net/2019/01/30/xstate-a-typescript-state-machine-with-a-lot-of-features
 */

object promiseExample:

  /* https://xstate.js.org/docs/guides/start.html#our-first-machine
  const promiseMachine = createMachine({
    id: 'promise',
    initial: 'pending',
    states: {
      pending: {
        on: {
          RESOLVE: { target: 'resolved' },
          REJECT: { target: 'rejected' }
        }
      },
      resolved: {
        type: 'final'
      },
      rejected: {
        type: 'final'
      }
    }
  });
   */

  // Possible encoding of the above
  enum Event:
    case Resolve
    case Reject

  case class Initial[A <: PromiseState]()
  case class On[A <: Event, B <: PromiseState](from: A, target: B)

  enum PromiseState:
    case Pending(on: (On[Event, PromiseState], On[Event, PromiseState]))
    case Resolved(`type`: PromiseState = Final)
    case Rejected(`type`: PromiseState = Final)
    case Final

  case class MachineConfig[A <: PromiseState](
      initial: Initial[A],
      states: List[PromiseState]
  )

  def createMachine = MachineConfig(
    initial = Initial[PromiseState.Pending](),
    states = List(
      PromiseState.Pending(
        on = (
          On(Event.Resolve, PromiseState.Resolved()),
          On(Event.Reject, PromiseState.Rejected())
        )
      ),
      PromiseState.Resolved(),
      PromiseState.Rejected()
    )
  )

object toggleExample:
  enum Toggle:
    case Active
    case Inactive

object lightBulbExample:
  /*
https://www.nearform.com/blog/making-complex-state-management-easy-with-xstate/

import { createMachine } from 'xstate';

const machine = createMachine({
  initial: "powered_off",
  states: {
    powered_on: {
      initial: "oscillating_off",
      on: {
        TURN_OFF: 'powered_off'
      },
      states: {
        oscillating_off: {
          on: {
            OSCILLATE_ON: 'oscillating_on'
          }
        },
        oscillating_on: {
          on: {
            OSCILLATE_OFF: 'oscillating_off'
          }
        }
      }
    },
    powered_off: {
      on: {
        TURN_ON: 'powered_on'
      }
    }
  }
});
   */

  enum LightBulb:
    case PoweredOff
    case PoweredOn
    case OscillatingOff
    case OscillatingOn

  import LightBulb.*

  case class Initial[A <: LightBulb](a: A)
  case class On[A <: LightBulb](from: A, to: A)

  case class MachineDefinition[A <: LightBulb](
      initial: A,
      states: List[A]
  )

  def states = MachineDefinition(
    initial = PoweredOff,
    states = List(
    )
  )

def createMachine[T](t: T): js.Any = ???
//facade.createMachine(json)
