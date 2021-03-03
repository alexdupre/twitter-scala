object RequestToken extends RestApp {

  checkParams(1, "<callback url>")

  val callbackUrl = args(0)

  run { client =>
    val resp = client.requestToken(callbackUrl)
    println(resp)
    println(s"https://api.twitter.com/oauth/authenticate?oauth_token=${resp.key}")
  }

}
