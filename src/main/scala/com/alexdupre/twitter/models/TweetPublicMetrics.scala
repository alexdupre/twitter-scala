package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Engagement metrics for the Tweet at the time of the request.
  */
case class TweetPublicMetrics(
    /* Number of times this Tweet has been Retweeted. */
    retweetCount: Int,
    /* Number of times this Tweet has been replied to. */
    replyCount: Int,
    /* Number of times this Tweet has been liked. */
    likeCount: Int,
    /* Number of times this Tweet has been quoted. */
    quoteCount: Option[Int] = None
)

object TweetPublicMetrics {
  implicit val format = Json.format[TweetPublicMetrics]
}
