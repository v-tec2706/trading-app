package trading.lib

import scala.reflect.ClassTag

import trading.lib.Consumer.{ Msg, MsgId }

import cats.{ Monad, MonadThrow }
import cats.effect.kernel.Deferred
import cats.syntax.all.*
import fs2.{ Pull, Stream }
import monocle.Iso

export Logger.redisLog

extension [F[_]: Monad, A](c: Consumer[F, A])
  def rewind(id: Consumer.MsgId, gate: Deferred[F, Unit]): Stream[F, Msg[A]] =
    Stream.eval(c.lastMsgId).flatMap { lastId =>
      c.receiveM(id).evalTap { msg =>
        gate.complete(()).whenA(lastId == Some(msg.id))
      }
    }

extension [F[_], A, B, C](src: Stream[F, Either[Either[Msg[A], Msg[B]], Msg[C]]])
  def union2: Stream[F, Msg[A | B | C]] =
    src.map {
      case Left(Left(ma))  => ma.asInstanceOf[Msg[A | B | C]]
      case Left(Right(mb)) => mb.asInstanceOf[Msg[A | B | C]]
      case Right(mc)       => mc.asInstanceOf[Msg[A | B | C]]
    }
