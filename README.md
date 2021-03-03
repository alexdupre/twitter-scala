# twitter-scala

[![Build Status](https://travis-ci.org/alexdupre/twitter-scala.png?branch=master)](https://travis-ci.org/alexdupre/twitter-scala)

A Scala library for the Twitter API: https://developer.twitter.com/en/docs/twitter-api

The library is in Early Access, as the Twitter API v2 that it supports, expect changes in the API/ABI.

The REST API can be used in both synchronous (blocking) and asynchronous (non-blocking) modes, thanks to the `sttp` backend's selection that is up to the library's user.

The library uses the new API v2 endpoints where possible, with fallback to v1 endpoints where required.

Only a small subset of the Twitter functionalities have been implemented:

- REST API:
  - Get recent tweets
  - Login with Twitter flow
  - Get user by id/username
  - Send simple Direct Messages
  - Get/add/remove stream rules

- Streaming API
  - Stream tweets in real-time

## Artifacts

The latest release of the library is compiled with Scala 2.13 only and supports all sttp backends.

| Version | Artifact Id             | HTTP Provider   | Json Provider | Scala |
| ------- | ----------------------- | --------------- | ------------- | ------|
| 0.1     | twitter                 | sttp3           | Play-Json     | 2.13  |

If you're using SBT, add the following line to your build file:

```scala
libraryDependencies += "com.alexdupre" %% "twitter" % "<version>"
```

## Examples

You can find many examples on how to use this library in the [src/test/scala](https://github.com/alexdupre/twitter-scala/tree/master/src/test/scala) directory, and you can directly run them, after entering your credentials in the `application.conf` file.

Two notable examples are:
- [GetAllRecentTweets](https://github.com/alexdupre/twitter-scala/blob/master/src/test/scala/GetAllRecentTweets.scala), that shows how to handle paginated endpoints with a `fold`-like function
- [StreamTweets](https://github.com/alexdupre/twitter-scala/blob/master/src/test/scala/StreamTweets.scala), that shows how to stream tweets with the `akka-backend`

