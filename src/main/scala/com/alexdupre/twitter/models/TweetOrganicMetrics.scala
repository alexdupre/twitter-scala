package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Organic nonpublic engagement metrics for the Tweet at the time of the request.
  */
case class TweetOrganicMetrics(
    /* Number of times this Tweet has been viewed. */
    impressionCount: Int,
    /* Number of times this Tweet has been Retweeted. */
    retweetCount: Int,
    /* Number of times this Tweet has been replied to. */
    replyCount: Int,
    /* Number of times this Tweet has been liked. */
    likeCount: Int,
    /* Number of times the user's profile from this Tweet has been clicked. */
    userProfileClicks: Int,
    /* Number of times links in this Tweet have been clicked. */
    urlLinkClicks: Option[Int] = None
)

object TweetOrganicMetrics {
  implicit val format = Json.format[TweetOrganicMetrics]
}
