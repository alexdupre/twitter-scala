package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class MentionEntity(
    /* Index (zero-based) at which position this entity starts. */
    start: Int,
    /* Index (zero-based) at which position this entity ends. */
    end: Int,
    /* The Twitter handle (screen name) of this user. */
    username: String
)

object MentionEntity {
  implicit val format = Json.format[MentionEntity]
}
