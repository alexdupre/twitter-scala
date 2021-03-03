import akka.actor.ActorSystem
import com.alexdupre.twitter.StreamingTwitterClient
import com.alexdupre.twitter.TwitterClient.{TweetExpansion, TweetField}
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.akkahttp.AkkaHttpBackend

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object StreamTweets extends App with Credentials {

  implicit val system  = ActorSystem()
  implicit val backend = AkkaHttpBackend.usingActorSystem(system)
  implicit val streams = AkkaStreams

  val tweetExpansions = Set(TweetExpansion.AuthorId, TweetExpansion.InReplyToUserId)
  val tweetFields     = Set(TweetField.AuthorId, TweetField.CreatedAt)

  val client = new StreamingTwitterClient[Future, AkkaStreams](consumerKey, bearerToken)

  val done = client.streamTweets(tweetExpansions, tweetFields).flatMap { source =>
    println(s"Connected! Streaming for 60 seconds...")
    source.takeWithin(1.minute).runForeach { bytestring =>
      val str = bytestring.utf8String.trim
      if (str.nonEmpty) { // ignore \r\n sent to keep alive the stream
        val t = client.parseStreamElement(str).expandedTweet
        println(s"${t.tweet.id} - ${t.tweet.createdAt
          .getOrElse("")} - ${t.author.map(u => s"@${u.username}").getOrElse("")} - ${t.tweet.text.takeWhile(_ != '\n')}")
      }
    }
  }
  done.onComplete { _ =>
    backend.close().andThen { case _ =>
      println("Disconnected")
      system.terminate()
    }
  }

}
