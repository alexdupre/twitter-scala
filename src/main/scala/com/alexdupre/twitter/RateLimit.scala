package com.alexdupre.twitter

import java.time.Instant

case class RateLimit(limit: Int, remaining: Int, reset: Instant)
