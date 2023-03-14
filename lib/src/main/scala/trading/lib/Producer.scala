package trading.lib

import java.nio.charset.StandardCharsets.UTF_8

import cats.Eq
import cats.effect.kernel.{ Async, Ref, Resource }
import cats.effect.std.Queue
import cats.syntax.all.*
import cats.{ Applicative, Parallel, Show }
import dev.profunktor.pulsar.{ Producer as PulsarProducer, * }
import fs2.kafka.{ KafkaProducer, ProducerSettings }
import io.circe.Encoder
import io.circe.syntax.*

trait Producer[F[_], A]:
  def send(a: A): F[Unit]
  def send(a: A, properties: Map[String, String]): F[Unit]
  def send(a: A, tx: Txn): F[Unit]
  def send(a: A, properties: Map[String, String], tx: Txn): F[Unit]

object Producer:
  def pulsar[F[_]: Async: Logger: Parallel, A: Encoder](
      client: Pulsar.T,
      topic: Topic.Single,
      settings: Option[PulsarProducer.Settings[F, A]] = None
  ): Resource[F, Producer[F, A]] =
    val _settings =
      settings
        .getOrElse(PulsarProducer.Settings[F, A]())
        .withLogger(Logger.pulsar[F, A]("out"))

    val encoder: A => Array[Byte] = _.asJson.noSpaces.getBytes(UTF_8)

    PulsarProducer.make[F, A](client, topic, encoder, _settings).map { p =>
      new:
        def send(a: A): F[Unit]                                           = p.send_(a)
        def send(a: A, properties: Map[String, String]): F[Unit]          = p.send_(a, properties)
        def send(a: A, tx: Txn): F[Unit]                                  = p.send_(a, tx.get)
        def send(a: A, properties: Map[String, String], tx: Txn): F[Unit] = p.send_(a, properties, tx.get)
    }
