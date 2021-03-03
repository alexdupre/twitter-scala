package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** The location tagged on the Tweet, if the user provided one.
  */
case class TweetGeo(
    coordinates: Option[Point] = None,
    /* The identifier for this place */
    placeId: Option[BigInt] = None
)

object TweetGeo {
  implicit val format = Json.format[TweetGeo]
}
