package com.alexdupre.twitter.models
import play.api.libs.json.Json

import java.net.URI

/** Represent the information for the URL image
  */
case class URLImage(
    /* A validly formatted URL. */
    url: Option[URI] = None,
    /* The height of the media in pixels */
    height: Option[Int] = None,
    /* The width of the media in pixels */
    width: Option[Int] = None
)

object URLImage {
  implicit val format = Json.format[URLImage]
}
