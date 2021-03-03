package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class TwitterError(code: Int, message: String)

object TwitterError {
  implicit val format = Json.format[TwitterError]
}
