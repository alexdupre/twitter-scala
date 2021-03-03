object RemoveStreamRules extends RestApp {

  checkParams(1, "<id> ...")

  val ids = args.map(BigInt.apply).toSet

  run { client =>
    val resp = client.removeStreamRules(ids)
    println(resp)
  }

}
