name := "twitter"

organization := "com.alexdupre"

version := "0.1"

scalaVersion := "2.13.5"

val sttpVersion = "3.1.6"

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client3" %% "core"              % sttpVersion,
  "com.softwaremill.sttp.client3" %% "slf4j-backend"     % sttpVersion,
  "com.softwaremill.sttp.client3" %% "play-json"         % sttpVersion,
  "com.typesafe.play"             %% "play-json"         % "2.9.2",
  "commons-codec"                  % "commons-codec"     % "1.15",
  "org.slf4j"                      % "slf4j-api"         % "1.7.30",
  "com.typesafe"                   % "config"            % "1.4.1"     % "test",
  "com.softwaremill.sttp.client3" %% "akka-http-backend" % sttpVersion % "test",
  "com.typesafe.akka"             %% "akka-stream"       % "2.6.13"    % "test",
  "ch.qos.logback"                 % "logback-classic"   % "1.2.3"     % "test"
)

fork in Test := true

publishTo := sonatypePublishToBundle.value

publishMavenStyle := true

licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))

sonatypeProjectHosting := Some(xerial.sbt.Sonatype.GitHubHosting("alexdupre", "twitter-scala", "Alex Dupre", "ale@FreeBSD.org"))

buildInfoKeys := Seq[BuildInfoKey](version)

buildInfoPackage := s"${organization.value}.${name.value}"

enablePlugins(BuildInfoPlugin)
