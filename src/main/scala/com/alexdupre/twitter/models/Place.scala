package com.alexdupre.twitter.models

import play.api.libs.json.Json

case class Place(
    /* The identifier for this place */
    id: BigInt,
    /* The human readable name of this place. */
    name: Option[String] = None,
    countryCode: Option[String] = None,
    placeType: Option[PlaceEnums.PlaceType] = None,
    fullName: String,
    country: Option[String] = None,
    containedWithin: Option[Seq[String]] = None,
    geo: Option[Geo] = None
)

object PlaceEnums {
  type PlaceType = PlaceType.Value
  object PlaceType extends Enumeration {
    val Poi          = Value("poi")
    val Neighborhood = Value("neighborhood")
    val City         = Value("city")
    val Admin        = Value("admin")
    val Country      = Value("country")
    val Unknown      = Value("unknown")

    implicit val format = Json.formatEnum(this)
  }
}

object Place {
  implicit val format = Json.format[Place]
}
