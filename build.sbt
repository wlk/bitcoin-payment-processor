name := "Bitcoin Payment Processor"

version := "1.0"

scalaVersion := "2.11.5"

resolvers ++= Seq(
  Resolver.mavenLocal,
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io",
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies <<= scalaVersion {   scala_version =>
  val sprayVersion = "1.3.2"
  val sprayJsonVersion = "1.3.1"
  val akkaVersion = "2.3.9"
  Seq(
    "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "io.spray"                 %% "spray-can"                            % sprayVersion,
    "io.spray"                 %% "spray-json"                           % sprayJsonVersion,
    "io.spray"                 %% "spray-routing"                        % sprayVersion,
    "io.spray"                 %% "spray-httpx"                          % sprayVersion,
    "io.spray"                 %% "spray-util"                           % sprayVersion,
    "io.spray"                 %% "spray-client"                         % sprayVersion,
    "org.bitcoinj" % "bitcoinj-core" % "0.13-SNAPSHOT",
    "org.slf4j" % "slf4j-api" % "1.7.10",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.clapper"              %% "grizzled-slf4j"                       % "1.0.2",
    "com.typesafe" % "config" % "1.2.1",
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
  )
}

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-feature"

scalacOptions += "-Xcheckinit"