import AssemblyKeys._

assemblySettings

jarName in assembly := "blockchainwriter.jar"

name := "blockchainwriter2"

version := "1.0"

scalaVersion := "2.11.4"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

val sprayVersion = "1.3.2"

val akkaVersion  = "2.3.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-contrib"                         % akkaVersion,
  "com.typesafe.akka"        %% "akka-slf4j"                           % akkaVersion,
  "com.typesafe.akka"        %% "akka-actor"                           % akkaVersion,
  "io.spray"                 % "spray-can"                             % sprayVersion,
  "io.spray"                 % "spray-routing"                         % sprayVersion,
  "io.spray"                 % "spray-httpx"                           % sprayVersion,
  "io.spray"                 % "spray-util"                            % sprayVersion,
  "io.spray"                 % "spray-client"                          % sprayVersion,
  "org.bitcoinj"             % "bitcoinj-core"                         % "0.12.2"
)