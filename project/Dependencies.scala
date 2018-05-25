import sbt._

object Dependencies {
  val AkkaVersion = "2.5.12"
  val AkkaHttpVersion = "10.1.1"

  lazy val AkkaActor = "com.typesafe.akka" %% "akka-actor" % AkkaVersion
  lazy val AkkaStream = "com.typesafe.akka" %% "akka-stream" % AkkaVersion
  lazy val AkkaPersistence = "com.typesafe.akka" %% "akka-persistence" % AkkaVersion
  lazy val AkkaHttp = "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
  lazy val AkkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion

  lazy val MongoScalaDriver = "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.1"
  lazy val KafkaStream = "com.typesafe.akka" %% "akka-stream-kafka" % "0.20"

  lazy val ScalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
}
