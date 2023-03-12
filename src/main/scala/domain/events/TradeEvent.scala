package domain.events

import domain.commands.TradeCommand
import domain.{*, given}

import cats.{Applicative, Eq, Show}
import cats.derived.*
import cats.syntax.all.*
import io.circe.Codec
import monocle.Traversal

import cats.Show
import io.circe.Codec

enum TradeEvent derives Codec.AsObject, Show:
  def id: EventId
  def cid: CorrelationId
  def command: TradeCommand
  def createdAt: Timestamp

  case CommandExecuted(
      id: EventId,
      cid: CorrelationId,
      command: TradeCommand,
      createdAt: Timestamp
  )

  case CommandRejected(
      id: EventId,
      cid: CorrelationId,
      command: TradeCommand,
      reason: Reason,
      createdAt: Timestamp
  )
