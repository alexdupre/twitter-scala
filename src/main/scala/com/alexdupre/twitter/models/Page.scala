package com.alexdupre.twitter.models

abstract class Page(meta: Option[PagingMetadata]) {
  def nextToken = meta.flatMap(_.nextToken)
}
