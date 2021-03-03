package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class StreamRulesResponse(data: Option[Seq[StreamRule]], meta: StreamRulesMeta, errors: Option[Seq[Problem]])

object StreamRulesResponse {
  implicit val format = Json.format[StreamRulesResponse]
}
