object GetUserById extends RestApp {

  checkParams(1, "<id>")

  val id = BigInt(args(0))

  run { client =>
    val resp = client.getUserById(id)
    println(resp)
  }

}
