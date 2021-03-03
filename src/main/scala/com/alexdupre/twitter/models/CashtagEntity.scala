package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class CashtagEntity(
    /* Index (zero-based) at which position this entity starts. */
    start: Int,
    /* Index (zero-based) at which position this entity ends. */
    end: Int,
    tag: String
)

object CashtagEntity {
  implicit val format = Json.format[CashtagEntity]
}
