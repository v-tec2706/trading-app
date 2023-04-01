package trading.trace

import scala.concurrent.duration.*
import trading.domain.{ *, given }
import trading.shared.*
import com.comcast.ip4s.*
import ciris.{ ConfigDecoder, ConfigValue }

import cats.effect.kernel.Async
import cats.syntax.all.*
import ciris.*
import com.comcast.ip4s.*
import dev.profunktor.pulsar.Config as PulsarConfig

final case class TracingConfig(
    httpPort: Port,
    pulsar: PulsarConfig,
    honeycombApiKey: Config.HoneycombApiKey
)

object Config:
  type HoneycombApiKey = HoneycombApiKey.Type
  object HoneycombApiKey extends Newtype[String]
  given ConfigDecoder[String, HoneycombApiKey] = ConfigDecoder[String].map(HoneycombApiKey(_))

  def load[F[_]: Async]: F[TracingConfig] =
    (
      env("HTTP_PORT").as[Port].default(port"9005"),
      env("PULSAR_URI").as[PulsarURI].default(PulsarURI("pulsar://localhost:6650")),
      env("HONEYCOMB_API_KEY").as[HoneycombApiKey]
    ).parMapN { (port, pulsarUri, apiKey) =>
      val pulsar =
        PulsarConfig.Builder
          .withTenant("public")
          .withNameSpace("default")
          .withURL(pulsarUri.value)
          .build
      TracingConfig(port, pulsar, apiKey)
    }.load[F]
