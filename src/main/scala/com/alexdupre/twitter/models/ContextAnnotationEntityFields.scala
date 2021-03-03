package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Represents the data for the context annotation entity.
  */
case class ContextAnnotationEntityFields(
    /* The unique id for a context annotation entity. */
    id: BigInt,
    /* Name of the context annotation entity. */
    name: Option[String] = None,
    /* Description of the context annotation entity. */
    description: Option[String] = None
)

object ContextAnnotationEntityFields {
  implicit val format = Json.format[ContextAnnotationEntityFields]
}
