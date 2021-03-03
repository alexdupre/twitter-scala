package com.alexdupre.twitter.models

import play.api.libs.json.Json

/** A [GeoJson Point](https://tools.ietf.org/html/rfc7946#section-3.1.2) geometry object.
  */
case class Point(
    `type`: PointEnums.Type,
    /* A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`. */
    coordinates: Seq[Double]
)

object PointEnums {

  type Type = Type.Value
  object Type extends Enumeration {
    val Point = Value("Point")

    implicit val format = Json.formatEnum(this)
  }

}

object Point {
  implicit val format = Json.format[Point]
}
