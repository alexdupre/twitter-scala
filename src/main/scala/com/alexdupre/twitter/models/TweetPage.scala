package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class TweetPage(data: Option[Seq[Tweet]], includes: Option[Expansions], meta: Option[PagingMetadata]) extends Page(meta) {
  def expandedTweets: Seq[ExpandedTweet] = {
    val tweets     = data.getOrElse(Nil)
    val expansions = includes.getOrElse(Expansions())
    tweets.map(tweet => new ExpandedTweet(tweet, expansions))
  }
}

object TweetPage {
  implicit val format = Json.format[TweetPage]
}
