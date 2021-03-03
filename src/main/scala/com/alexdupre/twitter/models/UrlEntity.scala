package com.alexdupre.twitter.models
import play.api.libs.json.Json

import java.net.URI

/** Represent the portion of text recognized as a URL, and its start and end position within the text.
  */
case class UrlEntity(
    /* Index (zero-based) at which position this entity starts. */
    start: Int,
    /* Index (zero-based) at which position this entity ends. */
    end: Int,
    /* A validly formatted URL. */
    url: URI,
    /* A validly formatted URL. */
    expandedUrl: Option[URI] = None,
    /* The URL as displayed in the Twitter client. */
    displayUrl: Option[String] = None,
    /* Fully resolved url */
    unwoundUrl: Option[URI] = None,
    /* HTTP Status Code. */
    status: Option[Int] = None,
    /* Title of the page the URL points to. */
    title: Option[String] = None,
    /* Description of the URL landing page. */
    description: Option[String] = None,
    images: Option[Seq[URLImage]] = None
)

object UrlEntity {
  implicit val format = Json.format[UrlEntity]
}
