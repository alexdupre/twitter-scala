import com.alexdupre.twitter.OAuthToken

object VerifyCredentials extends RestApp {

  checkParams(2, "<token> <secret>")

  val token = OAuthToken(args(0), args(1))

  run { client =>
    val resp = client.verifyCredentials(token)
    println(resp)
  }

}
