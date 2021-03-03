package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class Expansions(
    users: Option[Seq[User]] = None,
    tweets: Option[Seq[Tweet]] = None,
    places: Option[Seq[Place]] = None,
    media: Option[Seq[Media]] = None,
    polls: Option[Seq[Poll]] = None
) {

  def user(id: BigInt)           = users.flatMap(_.find(_.id == id))
  def user(username: String)     = users.flatMap(_.find(_.username == username))
  def tweet(id: BigInt)          = tweets.flatMap(_.find(_.id == id))
  def place(id: BigInt)          = places.flatMap(_.find(_.id == id))
  def attachedMedia(key: String) = media.flatMap(_.find(_.mediaKey.contains(key)))
  def attachedPoll(id: BigInt)   = polls.flatMap(_.find(_.id == id))

}

object Expansions {
  implicit val format = Json.format[Expansions]
}
