package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Expanded details for the URL specified in the user's profile, with start and end indices.
  */
case class UserEntitiesUrl(
    urls: Option[Seq[UrlEntity]] = None
)

object UserEntitiesUrl {
  implicit val format = Json.format[UserEntitiesUrl]
}
