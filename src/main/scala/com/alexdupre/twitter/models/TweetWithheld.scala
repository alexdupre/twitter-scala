package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Indicates withholding details for [withheld content](https://help.twitter.com/en/rules-and-policies/tweet-withheld-by-country).
  */
case class TweetWithheld(
    /* Indicates if the content is being withheld for on the basis of copyright infringement. */
    copyright: Boolean,
    /* Provides a list of countries where this content is not available. */
    countryCodes: Set[String],
    /* Indicates whether the content being withheld is the `tweet` or a `user`. */
    scope: Option[TweetWithheldEnums.Scope] = None
)

object TweetWithheldEnums {

  type Scope = Scope.Value
  object Scope extends Enumeration {
    val Tweet = Value("tweet")
    val User  = Value("user")

    implicit val format = Json.formatEnum(this)
  }

}

object TweetWithheld {
  implicit val format = Json.format[TweetWithheld]
}
