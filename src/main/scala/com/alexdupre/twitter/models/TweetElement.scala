package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class TweetElement(data: Tweet, includes: Option[Expansions], matchingRules: Seq[MatchingRule]) {
  def expandedTweet: ExpandedTweet = {
    val expansions = includes.getOrElse(Expansions())
    new ExpandedTweet(data, expansions)
  }
}

object TweetElement {
  implicit val format = Json.format[TweetElement]
}
