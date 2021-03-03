package com.alexdupre.twitter

import com.alexdupre.twitter.TwitterClient.{MediaField, PlaceField, PollField, TweetExpansion, TweetField, UserExpansion, UserField}
import com.alexdupre.twitter.models.{Errors, Page, Problem, ShortUserV1, StreamRule, StreamRulesResponse, TweetPage, UserLookup}
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.{HmacAlgorithms, HmacUtils}
import play.api.libs.json.{JsObject, JsValue, Json, Reads}
import sttp.client3._
import sttp.client3.logging.LoggingBackend
import sttp.client3.logging.slf4j.Slf4jLogger
import sttp.client3.playJson._
import sttp.model.Uri.QuerySegment.KeyValue
import sttp.model.internal.Rfc3986
import sttp.model.{HeaderNames, Uri}
import sttp.monad.syntax._

import java.time.Instant
import java.util.UUID

class TwitterClient[F[_]](consumerKey: ConsumerKey, bearerToken: String)(implicit backend: SttpBackend[F, Any]) {

  implicit protected val monad = backend.responseMonad

  protected val baseUrl   = "https://api.twitter.com"
  protected val v1BaseUrl = s"$baseUrl/1.1"
  protected val v2BaseUrl = s"$baseUrl/2"

  protected val logger = new Slf4jLogger(getClass.getName, backend.responseMonad)
  protected val loggingBackend =
    LoggingBackend(backend, logger, logRequestHeaders = false, logRequestBody = true, logResponseHeaders = false, logResponseBody = true)

  protected def uaRequest = basicRequest.header(HeaderNames.UserAgent, s"AlexDupre-Twitter/${BuildInfo.version}")

  protected def asJsonV2[T: Reads: IsOption] = asEither(asJsonAlways[Problem], asJsonAlways[T])
    .mapWithMetadata {
      case (Left(Left(DeserializationException(body, _))), meta) =>
        Left(TwitterUnexpectedErrorException(meta.code, meta.headers, body))
      case (Left(Right(problem)), meta)                            => Left(TwitterV2ErrorException(meta.code, meta.headers, problem))
      case (Right(Left(DeserializationException(body, jsErr))), _) => Left(TwitterDeserializationException(body, jsErr))
      case (Right(Right(result)), _)                               => Right(result)
    }
    .showAs("as json v2")

  protected def asJsonV1[T: Reads: IsOption] = asEither(asJsonAlways[Errors], asJsonAlways[T])
    .mapWithMetadata {
      case (Left(Left(DeserializationException(body, _))), meta) =>
        Left(TwitterUnexpectedErrorException(meta.code, meta.headers, body))
      case (Left(Right(errors)), meta)                             => Left(TwitterV1ErrorException(meta.code, meta.headers, errors))
      case (Right(Left(DeserializationException(body, jsErr))), _) => Left(TwitterDeserializationException(body, jsErr))
      case (Right(Right(result)), _)                               => Right(result)
    }
    .showAs("as json v1")

  protected def asFormData = asEither(asJsonAlways[Errors], asStringAlways)
    .mapWithMetadata {
      case (Left(Left(DeserializationException(body, _))), meta) =>
        Left(TwitterUnexpectedErrorException(meta.code, meta.headers, body))
      case (Left(Right(errors)), meta) => Left(TwitterV1ErrorException(meta.code, meta.headers, errors))
      case (Right(result), _) =>
        Right(
          result
            .split('&')
            .toList
            .flatMap(_.split('=') match {
              case Array(key, value) => Some(key -> value)
              case _                 => None
            })
            .toMap
        )
    }
    .showAs("as form data")

  protected def send[T](request: RequestT[Identity, Either[TwitterException, T], Any]): F[T] = {
    request.send(loggingBackend).map(_.body.fold(e => throw e, identity))
  }

  protected def sendAndParseJsonV2[T: Reads](request: RequestT[Identity, Either[String, String], Any]): F[T] = {
    send(request.response(asJsonV2[T]))
  }

  protected def sendAndParseJsonV1[T: Reads](request: RequestT[Identity, Either[String, String], Any]): F[T] = {
    send(request.response(asJsonV1[T]))
  }

  protected def sendAndParseFormData(request: RequestT[Identity, Either[String, String], Any]): F[Map[String, String]] = {
    send(request.response(asFormData))
  }

  protected def bearerGet[T: Reads](url: Uri): F[T] = {
    sendAndParseJsonV2[T](uaRequest.get(url).auth.bearer(bearerToken))
  }

  protected def bearerPost[T: Reads](url: Uri, payload: JsObject): F[T] = {
    sendAndParseJsonV2[T](uaRequest.post(url).body(payload).auth.bearer(bearerToken))
  }

  protected def oauthGet[T: Reads](url: Uri, credentials: OAuthToken): F[T] = {
    sendAndParseJsonV1[T](addOAuthHeader(uaRequest.get(url), credentials = Some(credentials)))
  }

  protected def oauthPostForm(
      url: Uri,
      formData: Map[String, String] = Map.empty,
      credentials: Option[OAuthToken] = None,
      callbackUrl: Option[String] = None
  ): F[Map[String, String]] = {
    sendAndParseFormData(addOAuthHeader(uaRequest.post(url), formData, credentials, callbackUrl).body(formData))
  }

  protected def oauthPost[T: Reads](url: Uri, payload: JsObject, credentials: OAuthToken): F[T] = {
    sendAndParseJsonV1[T](addOAuthHeader(uaRequest.post(url), credentials = Some(credentials)).body(payload))
  }

  protected def addOAuthHeader(
      request: Request[Either[String, String], Any],
      formParams: Map[String, String] = Map.empty,
      credentials: Option[OAuthToken] = None,
      callbackUrl: Option[String] = None
  ) = {
    val percentEncode = Rfc3986.encode(Rfc3986.Unreserved, false, true)(_)

    val oauthParams = List(callbackUrl.map("oauth_callback" -> _), credentials.map("oauth_token" -> _.key)).flatten ::: List(
      "oauth_consumer_key"     -> consumerKey.apiKey,
      "oauth_nonce"            -> UUID.randomUUID().toString.filterNot(_ == '-'),
      "oauth_signature_method" -> "HMAC-SHA1",
      "oauth_timestamp"        -> Instant.now.getEpochSecond.toString,
      "oauth_version"          -> "1.0"
    )
    val method      = request.method.method.toUpperCase
    val uri         = s"$baseUrl${request.uri.pathSegments.toString}"
    val queryParams = request.uri.querySegments.collect { case KeyValue(k, v, _, _) => k -> v }
    val params = (oauthParams ++ queryParams ++ formParams.toSeq).sorted
      .map { case (k, v) =>
        s"${percentEncode(k)}=${percentEncode(v)}"
      }
      .mkString("&")

    val signatureBase = s"$method&${percentEncode(uri)}&${percentEncode(params)}"
    val signingKey    = s"${percentEncode(consumerKey.apiSecret)}&${percentEncode(credentials.map(_.secret).getOrElse(""))}"
    val signature     = Base64.encodeBase64String(new HmacUtils(HmacAlgorithms.HMAC_SHA_1, signingKey).hmac(signatureBase))

    val authorizationToken = (("oauth_signature" -> signature) :: oauthParams).sorted
      .map { case (k, v) =>
        s"""${percentEncode(k)}="${percentEncode(v)}""""
      }
      .mkString(", ")
    request.header(HeaderNames.Authorization, s"OAuth $authorizationToken")
  }

  def getRecentTweets(
      query: String,
      expansions: Set[TweetExpansion] = Set.empty,
      tweetFields: Set[TweetField] = Set.empty,
      mediaFields: Set[MediaField] = Set.empty,
      placeFields: Set[PlaceField] = Set.empty,
      pollFields: Set[PollField] = Set.empty,
      userFields: Set[UserField] = Set.empty,
      sinceId: Option[BigInt] = None,
      untilId: Option[BigInt] = None,
      startTime: Option[Instant] = None,
      endTime: Option[Instant] = None,
      maxResults: Int = 10,
      nextToken: Option[String] = None
  ): F[TweetPage] = {
    val queryParams = Map(
      "query"        -> query,
      "expansions"   -> expansions.mkString(","),
      "tweet.fields" -> tweetFields.mkString(","),
      "user.fields"  -> userFields.mkString(","),
      "media.fields" -> mediaFields.mkString(","),
      "poll.fields"  -> pollFields.mkString(","),
      "place.fields" -> placeFields.mkString(","),
      "since_id"     -> sinceId,
      "until_id"     -> untilId,
      "start_time"   -> startTime,
      "end_time"     -> endTime,
      "max_results"  -> maxResults,
      "next_token"   -> nextToken
    )
    bearerGet[TweetPage](
      uri"$v2BaseUrl/tweets/search/recent?$queryParams"
    )
  }

  def foldPages[T <: Page, A](call: Option[String] => F[T])(zero: A)(f: (A, T) => A) = {
    def nextPage(curState: A, nextToken: Option[String]): F[A] = call(nextToken).flatMap { page =>
      val newState = f(curState, page)
      page.nextToken match {
        case Some(token) => nextPage(newState, Some(token))
        case None        => monad.unit(newState)
      }
    }
    nextPage(zero, None)
  }

  def getStreamRules(ids: Set[BigInt] = Set.empty): F[Seq[StreamRule]] = {
    val queryParams = Map("ids" -> ids.mkString(",")).filter(_._2.nonEmpty)
    bearerGet[StreamRulesResponse](uri"$v2BaseUrl/tweets/search/stream/rules?$queryParams").map(_.data.getOrElse(Nil))
  }

  def addStreamRule(value: String, tag: Option[String], dryRun: Boolean = false): F[StreamRulesResponse] = {
    val queryParams = Map("dry_run" -> dryRun).filter(_._2)
    val payload     = Json.obj("add" -> Seq(Json.obj("value" -> value, "tag" -> tag)))
    bearerPost[StreamRulesResponse](uri"$v2BaseUrl/tweets/search/stream/rules?$queryParams", payload)
  }

  def removeStreamRules(ids: Set[BigInt], dryRun: Boolean = false): F[StreamRulesResponse] = {
    val queryParams = Map("dry_run" -> dryRun).filter(_._2)
    val payload     = Json.obj("delete" -> Json.obj("ids" -> ids))
    bearerPost[StreamRulesResponse](uri"$v2BaseUrl/tweets/search/stream/rules?$queryParams", payload)
  }

  // Users

  def getUserById(
      id: BigInt,
      expansions: Set[UserExpansion] = Set.empty,
      userFields: Set[UserField] = Set.empty,
      tweetFields: Set[TweetField] = Set.empty
  ): F[UserLookup] = {
    val queryParams = Map(
      "expansions"   -> expansions.mkString(","),
      "tweet.fields" -> tweetFields.mkString(","),
      "user.fields"  -> userFields.mkString(",")
    )
    bearerGet[UserLookup](uri"$v2BaseUrl/users/$id?$queryParams")
  }

  def getUserByUsername(
      username: String,
      expansions: Set[UserExpansion] = Set.empty,
      userFields: Set[UserField] = Set.empty,
      tweetFields: Set[TweetField] = Set.empty
  ): F[UserLookup] = {
    val queryParams = Map(
      "expansions"   -> expansions.mkString(","),
      "tweet.fields" -> tweetFields.mkString(","),
      "user.fields"  -> userFields.mkString(",")
    )
    bearerGet[UserLookup](uri"$v2BaseUrl/users/by/username/$username?$queryParams")
  }

  // Login with Twitter

  def requestToken(callbackUrl: String, accessType: Option[AccessType.Value] = None): F[OAuthToken] = {
    val queryParams = Map("x_auth_access_type" -> accessType)
    oauthPostForm(uri"$baseUrl/oauth/request_token?$queryParams", callbackUrl = Some(callbackUrl)).map { result =>
      if (!result("oauth_callback_confirmed").toBoolean)
        sys.error("Unverified callback url") // unreachable code, it returns 403 forbidden in this case
      OAuthToken(result("oauth_token"), result("oauth_token_secret"))
    }
  }

  def getAccessToken(token: OAuthToken, verifier: String): F[OAuthToken] = {
    oauthPostForm(uri"$baseUrl/oauth/access_token", Map("oauth_verifier" -> verifier), Some(token)).map { result =>
      OAuthToken(result("oauth_token"), result("oauth_token_secret"))
    }
  }

  def verifyCredentials(
      credentials: OAuthToken,
      /*
      includeEntities: Boolean = false,
      skipStatus: Boolean = false,
       */
      includeEmail: Boolean = false
  ): F[ShortUserV1] = {
    val queryParams =
      Map( /*"include_entities" -> includeEntities, "skip_status" -> skipStatus,*/ "include_email" -> includeEmail).filter(_._2)
    oauthGet[ShortUserV1](uri"$v1BaseUrl/account/verify_credentials.json?$queryParams", credentials)
  }

  // Direct Messages

  def sendDirectMessage(credentials: OAuthToken, recipientId: BigInt, text: String): F[Unit] = {
    val payload = Json.obj(
      "event" -> Json.obj(
        "type"           -> "message_create",
        "message_create" -> Json.obj("target" -> Json.obj("recipient_id" -> recipientId), "message_data" -> Json.obj("text" -> text))
      )
    )
    oauthPost[JsValue](uri"$v1BaseUrl/direct_messages/events/new.json", payload, credentials).map(_ => ())
  }
}

object TwitterClient {

  type TweetExpansion = TweetExpansion.Value
  object TweetExpansion extends Enumeration {
    val AttachmentsPollIds         = Value("attachments.poll_ids")
    val AttachmentsMediaKeys       = Value("attachments.media_keys")
    val AuthorId                   = Value("author_id")
    val EntitiesMentionsUsername   = Value("entities.mentions.username")
    val GeoPlaceId                 = Value("geo.place_id")
    val InReplyToUserId            = Value("in_reply_to_user_id")
    val ReferencedTweetsId         = Value("referenced_tweets.id")
    val ReferencedTweetsIdAuthorId = Value("referenced_tweets.id.author_id")
  }

  type UserExpansion = UserExpansion.Value
  object UserExpansion extends Enumeration {
    val PinnedTweetId = Value("pinned_tweet_id")
  }

  type MediaField = MediaField.Value
  object MediaField extends Enumeration {
    val DurationMs       = Value("duration_ms")
    val Height           = Value("height")
    val MediaKey         = Value("media_key")
    val PreviewImageUrl  = Value("preview_image_url")
    val Type             = Value("type")
    val Url              = Value("url")
    val Width            = Value("width")
    val PublicMetrics    = Value("public_metrics")
    val NonPublicMetrics = Value("non_public_metrics")
    val OrganicMetrics   = Value("organic_metrics")
    val PromotedMetrics  = Value("promoted_metrics")
  }

  type PlaceField = PlaceField.Value
  object PlaceField extends Enumeration {
    val ContainedWithin = Value("contained_within")
    val Country         = Value("country")
    val CountryCode     = Value("country_code")
    val FullName        = Value("full_name")
    val Geo             = Value("geo")
    val Id              = Value("id")
    val Name            = Value("name")
    val PlaceType       = Value("place_type")
  }

  type PollField = PollField.Value
  object PollField extends Enumeration {
    val DurationMinutes = Value("duration_minutes")
    val EndDatetime     = Value("end_datetime")
    val Id              = Value("id")
    val Options         = Value("options")
    val VotingStatus    = Value("voting_status")
  }

  type TweetField = TweetField.Value
  object TweetField extends Enumeration {
    val Attachments        = Value("attachments")
    val AuthorId           = Value("author_id")
    val ContextAnnotations = Value("context_annotations")
    val ConversationId     = Value("conversation_id")
    val CreatedAt          = Value("created_at")
    val Entities           = Value("entities")
    val Geo                = Value("geo")
    val Id                 = Value("id")
    val InReplyToUserId    = Value("in_reply_to_user_id")
    val Lang               = Value("lang")
    val NonPublicMetrics   = Value("non_public_metrics")
    val PublicMetrics      = Value("public_metrics")
    val OrganicMetrics     = Value("organic_metrics")
    val PromotedMetrics    = Value("promoted_metrics")
    val PossiblySensitive  = Value("possibly_sensitive")
    val ReferencedTweets   = Value("referenced_tweets")
    val ReplySettings      = Value("reply_settings")
    val Source             = Value("source")
    val Text               = Value("text")
    val Withheld           = Value("withheld")
  }

  type UserField = UserField.Value
  object UserField extends Enumeration {
    val CreatedAt       = Value("created_at")
    val Description     = Value("description")
    val Entities        = Value("entities")
    val Id              = Value("id")
    val Location        = Value("location")
    val Name            = Value("name")
    val PinnedTweetId   = Value("pinned_tweet_id")
    val ProfileImageUrl = Value("profile_image_url")
    val Protected       = Value("protected")
    val PublicMetrics   = Value("public_metrics")
    val Url             = Value("url")
    val Username        = Value("username")
    val Verified        = Value("verified")
    val Withheld        = Value("withheld")
  }

}
