package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Nonpublic engagement metrics for the Tweet at the time of the request.
  */
case class TweetNonPublicMetrics(
    /* Number of times this Tweet has been viewed. */
    impressionCount: Option[Int] = None,
    /* Number of times the user's profile from this Tweet has been clicked. */
    userProfileClicks: Option[Int] = None,
    /* Number of times links in this Tweet have been clicked. */
    urlLinkClicks: Option[Int] = None
)

object TweetNonPublicMetrics {
  implicit val format = Json.format[TweetNonPublicMetrics]
}
