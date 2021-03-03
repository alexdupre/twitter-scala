package com.alexdupre.twitter.models

import play.api.libs.json.Json

import java.time.OffsetDateTime

case class StreamRulesMeta(sent: OffsetDateTime, summary: Option[StreamRulesSummary])

object StreamRulesMeta {
  implicit val format = Json.format[StreamRulesMeta]
}
