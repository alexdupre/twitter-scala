package com.alexdupre.twitter.models
import play.api.libs.json.Json

import java.time.OffsetDateTime

/** Represent a Poll attached to a Tweet
  */
case class Poll(
    /* Unique identifier of this poll. */
    id: BigInt,
    options: Seq[PollOption],
    votingStatus: Option[PollEnums.VotingStatus] = None,
    endDatetime: Option[OffsetDateTime] = None,
    durationMinutes: Option[Int] = None
)

object PollEnums {

  type VotingStatus = VotingStatus.Value
  object VotingStatus extends Enumeration {
    val Open   = Value("open")
    val Closed = Value("closed")

    implicit val format = Json.formatEnum(this)
  }

}

object Poll {
  implicit val format = Json.format[Poll]
}
