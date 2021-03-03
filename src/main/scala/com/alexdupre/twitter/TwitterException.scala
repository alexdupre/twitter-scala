package com.alexdupre.twitter

import com.alexdupre.twitter.models.{Errors, Problem}
import play.api.libs.json.JsError
import sttp.model.{Header, StatusCode}

import java.time.Instant

sealed abstract class TwitterException(message: String) extends RuntimeException(message)

sealed abstract class TwitterHttpException(headers: Seq[Header], message: String) extends TwitterException(message) {
  def rateLimit = for {
    limit     <- headers.find(_.name == "x-rate-limit-limit").map(_.value.toInt)
    remaining <- headers.find(_.name == "x-rate-limit-remaining").map(_.value.toInt)
    reset     <- headers.find(_.name == "x-rate-limit-reset").map(_.value.toLong).map(Instant.ofEpochSecond)
  } yield RateLimit(limit, remaining, reset)
}

// parsed HTTP error
case class TwitterV2ErrorException(status: StatusCode, headers: Seq[Header], error: Problem)
    extends TwitterHttpException(headers, s"${error.title}: ${error.detail.getOrElse(error.`type`)}")
case class TwitterV1ErrorException(status: StatusCode, headers: Seq[Header], errors: Errors)
    extends TwitterHttpException(headers, errors.errors.map(e => s"${e.message} (${e.code})").mkString(" - "))
// unparsed HTTP error
case class TwitterUnexpectedErrorException(status: StatusCode, headers: Seq[Header], body: String)
    extends TwitterHttpException(headers, s"[$status] ${body.takeWhile(_ != '\n')}")
// body deserialization error
case class TwitterDeserializationException(body: String, error: JsError) extends TwitterException(error.errors.mkString(","))
// parsed stream error
case class TwitterStreamErrorException(body: String, error: Problem)
    extends TwitterException(s"${error.title}: ${error.detail.getOrElse(error.`type`)}")
