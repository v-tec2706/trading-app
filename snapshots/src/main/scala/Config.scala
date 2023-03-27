package trading.snapshots

import java.util.UUID

import scala.concurrent.duration.*

import trading.domain.{ *, given }
import trading.lib.GenUUID

import cats.effect.kernel.Async
import cats.syntax.all.*
import ciris.*
import com.comcast.ip4s.*
import dev.profunktor.pulsar.Config as PulsarConfig
import ciris.ConfigDecoder.stringFiniteDurationConfigDecoder.given

final case class SnapshotsConfig(
    httpPort: Port,
    pulsar: PulsarConfig,
    redisUri: RedisURI,
    keyExpiration: KeyExpiration,
    appId: AppId
)

object Config:
  def load[F[_]: Async]: F[SnapshotsConfig] =
    GenUUID[F].make.flatMap { uuid =>
      (
        env("HTTP_PORT").as[Port].default(port"9002"),
        env("PULSAR_URI").as[PulsarURI].default(PulsarURI("pulsar://localhost:6650")),
        env("REDIS_URI").as[RedisURI].default(RedisURI("redis://localhost")),
        env("SNAPSHOT_KEY_EXPIRATION").as[KeyExpiration].default(KeyExpiration(1.hour))
      ).parMapN { (port, pulsarUri, redisUri, keyExp) =>
        val pulsar =
          PulsarConfig.Builder
            .withTenant("public")
            .withNameSpace("default")
            .withURL(pulsarUri.value)
            .build
        SnapshotsConfig(port, pulsar, redisUri, keyExp, AppId("snapshots", uuid))
      }.load[F]
    }
