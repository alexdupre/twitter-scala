import com.alexdupre.twitter.{ConsumerKey, OAuthToken}
import com.typesafe.config.ConfigFactory

trait Credentials {

  private val conf = ConfigFactory.load()

  val consumerKey = {
    val apiKey = conf.getString("twitter.consumerKey.apiKey")
    val apiSecret = conf.getString("twitter.consumerKey.apiSecret")
    ConsumerKey(apiKey, apiSecret)
  }

  val bearerToken = conf.getString("twitter.bearerToken")

  val accessToken = {
    val userKey = conf.getString("twitter.accessToken.userKey")
    val userSecret = conf.getString("twitter.accessToken.userSecret")
    OAuthToken(userKey, userSecret)
  }

}
