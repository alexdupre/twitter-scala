package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Describes a choice in a Poll object.
  */
case class PollOption(
    /* Position of this choice in the poll. */
    position: Int,
    /* The text of a poll choice. */
    label: String,
    /* Number of users who voted for this choice. */
    votes: Int
)

object PollOption {
  implicit val format = Json.format[PollOption]
}
