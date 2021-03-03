package com.alexdupre.twitter.models

class ExpandedTweet(val tweet: Tweet, expansions: Expansions) {

  def author                          = tweet.authorId.flatMap(expansions.user)
  def referencedTweet(id: BigInt)     = expansions.tweet(id).map(tweet => new ExpandedTweet(tweet, expansions))
  def inReplyToUser                   = tweet.inReplyToUserId.flatMap(expansions.user)
  def attachedMedia(key: String)      = expansions.attachedMedia(key)
  def attachedPoll(id: BigInt)        = expansions.attachedPoll(id)
  def geo                             = tweet.geo.flatMap(_.placeId).flatMap(expansions.place)
  def mentionedUser(username: String) = expansions.user(username)

}
