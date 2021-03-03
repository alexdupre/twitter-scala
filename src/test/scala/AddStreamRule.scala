object AddStreamRule extends RestApp {

  checkParams(1, "<rule text> [tag]")

  val rule = args(0)
  val tag = if (args.length == 2) Some(args(1)) else None

  run { client =>
    val resp = client.addStreamRule(rule, tag)
    println(resp)
  }

}
