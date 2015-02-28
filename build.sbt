name := "Bitcoin Payment Processor"

version := "1.0"

scalaVersion := "2.11.5"

resolvers ++= Seq(
  Resolver.mavenLocal,
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io",
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies <<= scalaVersion { scala_version =>
  val sprayVersion = "1.3.2"
  val sprayJsonVersion = "1.3.1"
  val akkaVersion = "2.3.9"
  val testDependencies = Seq(
    "org.specs2" % "specs2-core_2.11" % "3.0-M4-20150227014550-82f068c" % "test"
  )
  val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion
  )
  val sprayDependencies = Seq(
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-json" % sprayJsonVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-httpx" % sprayVersion,
    "io.spray" %% "spray-util" % sprayVersion,
    "io.spray" %% "spray-client" % sprayVersion
  )
  val loggingDependencies = Seq(
    "org.slf4j" % "slf4j-api" % "1.7.10",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.clapper" %% "grizzled-slf4j" % "1.0.2"
  )
  val otherDependencies = Seq(
    "org.bitcoinj" % "bitcoinj-core" % "0.13-SNAPSHOT"
  )

  otherDependencies ++ akkaDependencies ++ sprayDependencies ++ loggingDependencies // ++ testDependencies

}

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-feature"

scalacOptions += "-Xcheckinit"

scalacOptions += "-Xlint"