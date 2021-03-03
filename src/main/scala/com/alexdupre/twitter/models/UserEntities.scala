package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** A list of metadata found in the user's profile description.
  */
case class UserEntities(
    url: Option[UserEntitiesUrl] = None,
    description: Option[FullTextEntities] = None
)

object UserEntities {
  implicit val format = Json.format[UserEntities]
}
