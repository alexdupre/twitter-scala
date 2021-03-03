package com.alexdupre.twitter.models

import play.api.libs.json.{JsObject, Json}

import java.net.URI

/** An HTTP Problem Details object, as defined in IETF RFC 7807 (https://tools.ietf.org/html/rfc7807).
  */
case class Problem(
    `type`: URI,
    title: String,
    detail: Option[String],
    errors: Option[Seq[JsObject]]
)

object Problem {
  implicit val format = Json.format[Problem]
}
