package trading.events

import trading.commands.SwitchCommand
import trading.{*, given}

import cats.{Applicative, Show}
import cats.derived.*
import cats.syntax.all.*
import io.circe.Codec
import monocle.Traversal

enum SwitchEvent derives Codec.AsObject, Show:
  def id: EventId
  def cid: CorrelationId
  def createdAt: Timestamp

  case Started(
      id: EventId,
      cid: CorrelationId,
      createdAt: Timestamp
  )

  case Stopped(
      id: EventId,
      cid: CorrelationId,
      createdAt: Timestamp
  )

  case Ignored(
      id: EventId,
      cid: CorrelationId,
      createdAt: Timestamp
  )
