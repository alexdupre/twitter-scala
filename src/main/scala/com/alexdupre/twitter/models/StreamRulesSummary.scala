package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class StreamRulesSummary(
    created: Option[Int],
    notCreated: Option[Int],
    valid: Option[Int],
    invalid: Option[Int],
    deleted: Option[Int],
    notDeleted: Option[Int]
)

object StreamRulesSummary {
  implicit val format = Json.format[StreamRulesSummary]
}
