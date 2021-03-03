package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Promoted nonpublic engagement metrics for the Tweet at the time of the request.
  */
case class TweetPromotedMetrics(
    /* Number of times this Tweet has been viewed. */
    impressionCount: Option[Int] = None,
    /* Number of times this Tweet has been liked. */
    likeCount: Option[Int] = None,
    /* Number of times this Tweet has been replied to. */
    replyCount: Option[Int] = None,
    /* Number of times this Tweet has been Retweeted. */
    retweetCount: Option[Int] = None,
    /* Number of times the user's profile from this Tweet has been clicked. */
    userProfileClicks: Option[Int] = None,
    /* Number of times links in this Tweet have been clicked. */
    urlLinkClicks: Option[Int] = None
)

object TweetPromotedMetrics {
  implicit val format = Json.format[TweetPromotedMetrics]
}
