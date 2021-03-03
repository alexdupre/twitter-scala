package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** Annotation inferred from the tweet text.
  */
case class ContextAnnotation(
    domain: ContextAnnotationDomainFields,
    entity: ContextAnnotationEntityFields
)

object ContextAnnotation {
  implicit val format = Json.format[ContextAnnotation]
}
