package trading.commands

import cats.{Eq, Show}
import io.circe.Codec
import trading.{*, given}

import cats.{Applicative, Eq, Show}
import cats.derived.*
import cats.syntax.all.*
import io.circe.Codec
import monocle.Traversal

enum SwitchCommand derives Codec.AsObject, Eq, Show:
  def id: CommandId
  def cid: CorrelationId
  def createdAt: Timestamp

  case Start(
      id: CommandId,
      cid: CorrelationId,
      createdAt: Timestamp
  )

  case Stop(
      id: CommandId,
      cid: CorrelationId,
      createdAt: Timestamp
  )
