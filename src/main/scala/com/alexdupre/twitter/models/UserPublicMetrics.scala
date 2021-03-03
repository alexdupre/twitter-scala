package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** A list of metrics for this user
  */
case class UserPublicMetrics(
    /* Number of users who are following this user. */
    followersCount: Int,
    /* Number of users this user is following. */
    followingCount: Int,
    /* The number of Tweets (including Retweets) posted by this user. */
    tweetCount: Int,
    /* The number of lists that include this user. */
    listedCount: Int
)

object UserPublicMetrics {
  implicit val format = Json.format[UserPublicMetrics]
}
