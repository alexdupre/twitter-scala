import com.alexdupre.twitter.TwitterClient.{TweetExpansion, TweetField}
import com.alexdupre.twitter.models.ExpandedTweet

object GetAllRecentTweets extends RestApp {

  checkParams(1, "<query>")

  val query = args(0)

  val tweetExpansions = Set(TweetExpansion.AuthorId, TweetExpansion.InReplyToUserId)
  val tweetFields = Set(TweetField.AuthorId, TweetField.CreatedAt)

  run { client =>
    val resp = client.foldPages(token => client.getRecentTweets(query, expansions = tweetExpansions, tweetFields = tweetFields, maxResults = 100, nextToken = token))(Seq.empty[ExpandedTweet]) {
      case (acc, page) =>
        page.expandedTweets.foreach { t =>
          println(s"${t.tweet.id} - ${t.tweet.createdAt.getOrElse("")} - ${t.author.map(u => s"@${u.username}").getOrElse("")} - ${t.tweet.text.takeWhile(_ != '\n')}")
        }
        page.expandedTweets ++ acc
    }
    println(s"Total Tweets: ${resp.length}")
  }

}
