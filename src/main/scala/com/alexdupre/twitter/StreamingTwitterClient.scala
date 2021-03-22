package com.alexdupre.twitter

import com.alexdupre.twitter.TwitterClient.{MediaField, PlaceField, PollField, TweetExpansion, TweetField, UserField}
import com.alexdupre.twitter.models.{Problem, TweetElement}
import play.api.libs.json.{JsError, JsSuccess, Json}
import sttp.capabilities.Streams
import sttp.client3._
import sttp.client3.logging.LoggingBackend
import sttp.client3.playJson._
import sttp.model.Uri
import sttp.monad.syntax._

import scala.concurrent.duration._

class StreamingTwitterClient[F[_], P <: Streams[P]](consumerKey: ConsumerKey, bearerToken: String)(implicit
    backend: SttpBackend[F, P],
    val s: P
) extends TwitterClient[F](consumerKey, bearerToken)(backend) {

  protected val streamLoggingBackend =
    LoggingBackend(backend, logger, logRequestHeaders = false, logRequestBody = false, logResponseHeaders = false, logResponseBody = false)

  protected def asStreamV2 = asEither(asJsonAlways[Problem], asStreamAlwaysUnsafe(s))
    .mapWithMetadata {
      case (Left(Left(DeserializationException(body, _))), meta) =>
        Left(TwitterUnexpectedErrorException(meta.code, meta.headers, body))
      case (Left(Right(problem)), meta) => Left(TwitterV2ErrorException(meta.code, meta.headers, problem))
      case (Right(stream), _)           => Right(stream)
    }

  protected def bearerStream(url: Uri) = {
    val resp = uaRequest
      .get(url)
      .auth
      .bearer(bearerToken)
      .response(asStreamV2)
      .readTimeout(25.seconds)
      .send(streamLoggingBackend)
    resp.map(_.body.fold(e => throw e, identity))
  }

  def streamTweets(
      expansions: Set[TweetExpansion] = Set.empty,
      tweetFields: Set[TweetField] = Set(TweetField.Id, TweetField.Text),
      mediaFields: Set[MediaField] = Set.empty,
      placeFields: Set[PlaceField] = Set.empty,
      pollFields: Set[PollField] = Set.empty,
      userFields: Set[UserField] = Set.empty
  ) = {
    val queryParams = Map(
      "expansions"   -> expansions.mkString(","),
      "tweet.fields" -> tweetFields.mkString(","),
      "user.fields"  -> userFields.mkString(","),
      "media.fields" -> mediaFields.mkString(","),
      "poll.fields"  -> pollFields.mkString(","),
      "place.fields" -> placeFields.mkString(",")
    )
    bearerStream(uri"$v2BaseUrl/tweets/search/stream?$queryParams")
  }

  def parseStreamElement(body: String) = Json.parse(body).validate[TweetElement] match {
    case e: JsError            => throw TwitterDeserializationException(body, e)
    case JsSuccess(element, _) => element
  }

}
