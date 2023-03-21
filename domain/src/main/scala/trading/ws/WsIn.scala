package trading.ws

import trading.domain.*

import cats.Show
import cats.derived.*
import io.circe.Codec
import trading.shared.Symbol

enum WsIn derives Codec.AsObject, Show:
  case Close
  case Heartbeat
  case Subscribe(symbol: Symbol)
  case Unsubscribe(symbol: Symbol)
