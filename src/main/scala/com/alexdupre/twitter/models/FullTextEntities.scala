package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class FullTextEntities(
    urls: Option[Seq[UrlEntity]] = None,
    hashtags: Option[Seq[HashtagEntity]] = None,
    mentions: Option[Seq[MentionEntity]] = None,
    cashtags: Option[Seq[CashtagEntity]] = None
)

object FullTextEntities {
  implicit val format = Json.format[FullTextEntities]
}
