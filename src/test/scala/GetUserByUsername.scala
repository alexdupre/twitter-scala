object GetUserByUsername extends RestApp {

  checkParams(1, "<username>")

  val username = args(0)

  run { client =>
    val resp = client.getUserByUsername(username)
    println(resp)
  }

}
