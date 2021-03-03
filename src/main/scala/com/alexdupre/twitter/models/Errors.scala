package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class Errors(errors: Seq[TwitterError])

object Errors {
  implicit val format = Json.format[Errors]
}
