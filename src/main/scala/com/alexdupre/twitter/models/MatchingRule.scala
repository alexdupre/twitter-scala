package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class MatchingRule(id: BigInt, tag: Option[String])

object MatchingRule {
  implicit val format = Json.format[MatchingRule]
}
