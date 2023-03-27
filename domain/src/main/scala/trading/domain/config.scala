package trading.domain

import java.time.Instant
import java.util.UUID
import cats.syntax.all.*
import ciris.{ ConfigDecoder, ConfigValue }
import com.comcast.ip4s.*

import scala.concurrent.duration.{ Duration, FiniteDuration }

export CirisOrphan.given

object CirisOrphan:
  given ConfigDecoder[String, Instant] =
    ConfigDecoder[String].mapOption("java.time.Instant")(s => Either.catchNonFatal(Instant.parse(s)).toOption)

  given ConfigDecoder[String, UUID] =
    ConfigDecoder[String].mapOption("java.util.UUID")(s => Either.catchNonFatal(UUID.fromString(s)).toOption)

  given ConfigDecoder[String, Host] =
    ConfigDecoder[String].mapOption("com.comcast.ip4s.Host")(Host.fromString)

  given ConfigDecoder[String, Port] =
    ConfigDecoder[String].mapOption("com.comcast.ip4s.Port")(Port.fromString)

  given ConfigDecoder[String, PulsarURI] = ConfigDecoder[String].map(PulsarURI(_))

  given ConfigDecoder[String, RedisURI] = ConfigDecoder[String].map(RedisURI(_))

  given ConfigDecoder[String, KeyExpiration] = ConfigDecoder.stringFiniteDurationConfigDecoder.map(KeyExpiration(_))
