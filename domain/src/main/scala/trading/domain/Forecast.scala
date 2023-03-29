package trading.domain

import cats.Show
import cats.derived.*
import io.circe.Codec
import trading.shared.Symbol

final case class Forecast(
    id: ForecastId,
    symbol: Symbol,
    tag: ForecastTag,
    description: ForecastDescription,
    score: ForecastScore
) derives Codec.AsObject, Show
