package com.alexdupre.twitter.models

import play.api.libs.json.{JsObject, Json}

case class Geo(
    `type`: GeoEnums.Type,
    bbox: Seq[Double],
    geometry: Option[Point] = None,
    properties: JsObject
)

object GeoEnums {

  type Type = Type.Value
  object Type extends Enumeration {
    val Feature = Value("Feature")

    implicit val format = Json.formatEnum(this)
  }

}

object Geo {
  implicit val format = Json.format[Geo]
}
