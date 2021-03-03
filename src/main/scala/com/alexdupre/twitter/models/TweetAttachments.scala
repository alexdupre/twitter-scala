package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Specifies the type of attachments (if any) present in this Tweet.
  */
case class TweetAttachments(
    /* A list of Media Keys for each one of the media attachments (if media are attached). */
    mediaKeys: Option[Seq[String]] = None,
    /* A list of poll IDs (if polls are attached). */
    pollIds: Option[Seq[String]] = None
)

object TweetAttachments {
  implicit val format = Json.format[TweetAttachments]
}
