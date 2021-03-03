package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Indicates withholding details for [withheld content](https://help.twitter.com/en/rules-and-policies/tweet-withheld-by-country).
  */
case class UserWithheld(
    /* Provides a list of countries where this content is not available. */
    countryCodes: Set[String],
    /* Indicates that the content being withheld is a `user`. */
    scope: Option[UserWithheldEnums.Scope] = None
)

object UserWithheldEnums {

  type Scope = Scope.Value
  object Scope extends Enumeration {
    val User = Value("user")

    implicit val format = Json.formatEnum(this)
  }

}

object UserWithheld {
  implicit val format = Json.format[UserWithheld]
}
