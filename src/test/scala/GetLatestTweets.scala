import com.alexdupre.twitter.TwitterClient.{TweetExpansion, TweetField}

object GetLatestTweets extends RestApp {

  checkParams(1, "<query>")

  val query = args(0)

  val tweetExpansions = Set(TweetExpansion.AuthorId, TweetExpansion.InReplyToUserId)
  val tweetFields     = Set(TweetField.AuthorId, TweetField.CreatedAt)

  run { client =>
    val resp = client.getRecentTweets(query, expansions = tweetExpansions, tweetFields = tweetFields)
    resp.expandedTweets.foreach { t =>
      println(s"${t.tweet.id} - ${t.tweet.createdAt.getOrElse("")} - ${t.author.map(u => s"@${u.username}").getOrElse("")} - ${t.tweet.text
        .takeWhile(_ != '\n')}")
    }
  }

}
