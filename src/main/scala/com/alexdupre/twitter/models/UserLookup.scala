package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class UserLookup(data: Option[User], includes: Option[Expansions], errors: Option[Seq[Problem]])

object UserLookup {
  implicit val format = Json.format[UserLookup]
}
