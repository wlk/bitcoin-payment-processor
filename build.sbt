import sbtassembly.Plugin.AssemblyKeys._

assemblySettings

scalariformSettings

jarName in assembly := "blockchainwriter.jar"

name := "blockchainwriter"

version := "1.0"

scalaVersion := "2.11.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

val sprayVersion = "1.3.1"

val akkaVersion = "2.3.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka"     %% "akka-contrib"     % akkaVersion,
  "com.typesafe.akka"     %% "akka-slf4j"       % akkaVersion,
  "com.typesafe.akka"     %% "akka-actor"       % akkaVersion,
  "io.spray"              %% "spray-can"        % sprayVersion,
  "io.spray"              %% "spray-routing"    % sprayVersion,
  "io.spray"              %% "spray-httpx"      % sprayVersion,
  "io.spray"              %% "spray-util"       % sprayVersion,
  "io.spray"              %% "spray-client"     % sprayVersion,
  "org.bitcoinj"          % "bitcoinj-core"     % "0.12.2",
  "org.scala-sbt"         % "command"           % "0.13.6",
  "org.slf4j"             % "slf4j-api"         % "1.7.7",
  "org.slf4j"             % "log4j-over-slf4j"  % "1.7.7",
  "ch.qos.logback"        % "logback-classic"   % "1.1.2",
  "com.typesafe"          % "config"            % "1.2.1",
  "io.dropwizard.metrics" % "metrics-core"      % "3.1.0",
  "io.dropwizard.metrics" % "metrics-jvm"       % "3.1.0"
  )

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"