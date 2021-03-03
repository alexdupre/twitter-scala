package com.alexdupre.twitter.models

import play.api.libs.json.Json

import java.net.URI
import java.time.OffsetDateTime

/** The Twitter User object
  */
case class User(
    /* Unique identifier of this User. */
    id: BigInt,
    /* Creation time of this user. */
    createdAt: Option[OffsetDateTime] = None,
    /* The friendly name of this user, as shown on their profile. */
    name: String,
    /* The Twitter handle (screen name) of this user. */
    username: String,
    /* Indicates if this user has chosen to protect their Tweets (in other words, if this user's Tweets are private). */
    `protected`: Option[Boolean] = None,
    /* Indicate if this user is a verified Twitter User. */
    verified: Option[Boolean] = None,
    withheld: Option[UserWithheld] = None,
    /* The URL to the profile image for this user. */
    profileImageUrl: Option[URI] = None,
    /* The location specified in the user's profile, if the user provided one. As this is a freeform value, it may not indicate a valid location, but it may be fuzzily evaluated when performing searches with location queries. */
    location: Option[String] = None,
    /* The URL specified in the user's profile. */
    url: Option[String] = None,
    /* The text of this user's profile description (also known as bio), if the user provided one. */
    description: Option[String] = None,
    entities: Option[UserEntities] = None,
    /* Unique identifier of this Tweet. This is returned as a string in order to avoid complications with languages and tools that cannot handle large integers. */
    pinnedTweetId: Option[BigInt] = None,
    publicMetrics: Option[UserPublicMetrics] = None
)

object User {
  implicit val format = Json.format[User]
}
