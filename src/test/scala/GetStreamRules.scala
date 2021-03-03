object GetStreamRules extends RestApp {

  run { client =>
    val resp = client.getStreamRules()
    resp.foreach(println)
  }

}
