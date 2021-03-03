package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class PagingMetadata(newestId: Option[BigInt], oldestId: Option[BigInt], resultCount: Int, nextToken: Option[String])

object PagingMetadata {
  implicit val format = Json.format[PagingMetadata]
}
