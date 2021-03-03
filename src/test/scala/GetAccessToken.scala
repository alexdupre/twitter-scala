import com.alexdupre.twitter.OAuthToken

object GetAccessToken extends RestApp {

  checkParams(3, "<token> <secret> <verifier>")

  val token = OAuthToken(args(0), args(1))
  val verifier = args(2)

  run { client =>
    val resp = client.getAccessToken(token, verifier)
    println(resp)
  }

}
