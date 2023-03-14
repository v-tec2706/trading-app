package trading.lib

import cats.effect.kernel.Sync
import cats.syntax.functor.*

import java.util.UUID

trait GenUUID[F[_]]:
  def make: F[UUID]

object GenUUID:
  def apply[F[_]: GenUUID]: GenUUID[F] = summon

  given [F[_]: Sync]: GenUUID[F] with
    def make: F[UUID] = Sync[F].delay(UUID.randomUUID())
