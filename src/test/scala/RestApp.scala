import com.alexdupre.twitter.TwitterClient
import sttp.client3.{HttpURLConnectionBackend, Identity}

abstract class RestApp extends App with Credentials {

  def checkParams(count: Int, description: String) = if (args.length < count) {
    println(s"Missing parameter: ${getClass.getName.dropRight(1)} $description")
    System.exit(0)
  }

  def run(f: TwitterClient[Identity] => Unit) = {
    implicit val backend = HttpURLConnectionBackend()
    val client = new TwitterClient(consumerKey, bearerToken)
    try {
      f(client)
    } finally {
      backend.close()
    }
  }

}
