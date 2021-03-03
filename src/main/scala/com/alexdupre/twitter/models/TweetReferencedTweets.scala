package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class TweetReferencedTweets(
    `type`: TweetReferencedTweetsEnums.Type,
    /* Unique identifier of this Tweet. */
    id: BigInt
)

object TweetReferencedTweetsEnums {

  type Type = Type.Value
  object Type extends Enumeration {
    val Retweeted = Value("retweeted")
    val Quoted    = Value("quoted")
    val RepliedTo = Value("replied_to")

    implicit val format = Json.formatEnum(this)
  }

}

object TweetReferencedTweets {
  implicit val format = Json.format[TweetReferencedTweets]
}
