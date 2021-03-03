package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class HashtagEntity(
    /* Index (zero-based) at which position this entity starts. */
    start: Int,
    /* Index (zero-based) at which position this entity ends. */
    end: Int,
    /* The text of the Hashtag */
    tag: String
)

object HashtagEntity {
  implicit val format = Json.format[HashtagEntity]
}
