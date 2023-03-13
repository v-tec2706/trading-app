package trading.commands

import cats.{Eq, Show}
import io.circe.Codec

import trading.{*, given}

import cats.{Applicative, Eq, Show}
import cats.derived.*
import cats.syntax.all.*
import io.circe.Codec
import monocle.Traversal
import trading.shared.Symbol

enum TradeCommand derives Codec.AsObject, Eq, Show:
  def id: CommandId
  def cid: CorrelationId
  def symbol: Symbol
  def createdAt: Timestamp

  case Create(
      id: CommandId,
      cid: CorrelationId,
      symbol: Symbol,
      tradeAction: TradeAction,
      price: Price,
      quantity: Quantity,
      source: Source,
      createdAt: Timestamp
  )

  case Update(
      id: CommandId,
      cid: CorrelationId,
      symbol: Symbol,
      tradeAction: TradeAction,
      price: Price,
      quantity: Quantity,
      source: Source,
      createdAt: Timestamp
  )

  case Delete(
      id: CommandId,
      cid: CorrelationId,
      symbol: Symbol,
      tradeAction: TradeAction,
      price: Price,
      source: Source,
      createdAt: Timestamp
  )
