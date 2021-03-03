package com.alexdupre.twitter

import play.api.libs.json.{JsonConfiguration, JsonNaming}

package object models {
  implicit val config = JsonConfiguration(JsonNaming.SnakeCase)
}
