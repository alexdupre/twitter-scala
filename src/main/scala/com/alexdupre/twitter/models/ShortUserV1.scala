package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class ShortUserV1(id: BigInt, name: String, screenName: String, email: Option[String])

object ShortUserV1 {
  implicit val format = Json.format[ShortUserV1]
}
