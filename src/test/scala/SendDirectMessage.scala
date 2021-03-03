import com.alexdupre.twitter.OAuthToken

object SendDirectMessage extends RestApp {

  checkParams(2, "<recipient id> <text> [<token> <secret>]")

  val recipientId = BigInt(args(0))
  val text = args(1)
  val token = if (args.length == 4) OAuthToken(args(2), args(3)) else accessToken

  run { client =>
    client.sendDirectMessage(token, recipientId, text)
    println("Message Sent!")
  }

}
