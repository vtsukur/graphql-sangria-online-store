organization := "org.vtsukur.graphql.demo"
name := "graphql-sangria-online-store"
version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",

  "com.softwaremill.sttp" %% "core" % "1.1.12",
  "com.softwaremill.sttp" %% "circe" % "1.1.12",

  "io.circe" %% "circe-core" % "0.9.3",
  "io.circe" %% "circe-generic" % "0.9.3",
  "io.circe" %% "circe-parser" % "0.9.3",
  "io.circe" %% "circe-optics" % "0.9.3",

  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
)
