package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Represents the data for the context annotation domain.
  */
case class ContextAnnotationDomainFields(
    /* The unique id for a context annotation domain. */
    id: BigInt,
    /* Name of the context annotation domain. */
    name: Option[String] = None,
    /* Description of the context annotation domain. */
    description: Option[String] = None
)

object ContextAnnotationDomainFields {
  implicit val format = Json.format[ContextAnnotationDomainFields]
}
