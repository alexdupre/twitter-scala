package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class StreamRule(id: BigInt, value: String, tag: Option[String])

object StreamRule {
  implicit val format = Json.format[StreamRule]
}
