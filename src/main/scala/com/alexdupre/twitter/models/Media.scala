package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class Media(
    /* The Media Key identifier for this attachment. */
    mediaKey: Option[String] = None,
    /* The height of the media in pixels */
    height: Option[Int] = None,
    /* The width of the media in pixels */
    width: Option[Int] = None
)

object Media {
  implicit val format = Json.format[Media]
}
