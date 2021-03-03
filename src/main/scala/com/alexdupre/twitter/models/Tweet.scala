package com.alexdupre.twitter.models

import play.api.libs.json.Json

import java.time.OffsetDateTime

case class Tweet(
    /* Unique identifier of this Tweet. */
    id: BigInt,
    /* Creation time of the Tweet. */
    createdAt: Option[OffsetDateTime] = None,
    /* The content of the Tweet. */
    text: String,
    /* Unique identifier of this User. */
    authorId: Option[BigInt] = None,
    /* Unique identifier of this User. */
    inReplyToUserId: Option[BigInt] = None,
    /* A list of Tweets this Tweet refers to. For example, if the parent Tweet is a Retweet, a Quoted Tweet or a Reply, it will include the related Tweet referenced to by its parent. */
    referencedTweets: Option[Seq[TweetReferencedTweets]] = None,
    attachments: Option[TweetAttachments] = None,
    contextAnnotations: Option[Seq[ContextAnnotation]] = None,
    withheld: Option[TweetWithheld] = None,
    geo: Option[TweetGeo] = None,
    entities: Option[FullTextEntities] = None,
    publicMetrics: Option[TweetPublicMetrics] = None,
    /* Indicates if this Tweet contains URLs marked as sensitive, for example content suitable for mature audiences. */
    possiblySensitive: Option[Boolean] = None,
    /* Language of the Tweet, if detected by Twitter. Returned as a BCP47 language tag. */
    lang: Option[String] = None,
    /* The name of the app the user Tweeted from. */
    source: Option[String] = None,
    nonPublicMetrics: Option[TweetNonPublicMetrics] = None,
    promotedMetrics: Option[TweetPromotedMetrics] = None,
    organicMetrics: Option[TweetOrganicMetrics] = None
)

object Tweet {
  implicit val format = Json.format[Tweet]
}
