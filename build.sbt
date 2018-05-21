import Dependencies._

lazy val commonSettings = Seq(
  scalaVersion := "2.12.6",
  organization := "com.github.veinhorn"
)

lazy val root = (project in file(".")).
  settings(
    commonSettings,
    name := "nocaptcha",
    version := "0.1.0",
    libraryDependencies += ScalaTest % Test
  )

lazy val rest = (project in file("rest"))
  .settings(
    commonSettings,
    name := "rest",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      AkkaActor,
      AkkaStream,
      AkkaHttp,
      AkkaHttpSprayJson,
      MongoScalaDriver,
      KafkaStream,
      ScalaTest,
      "com.github.Opetushallitus" % "scala-schema" % "2.23.0_2.12"
    )
  )

lazy val webapp = (project in file("webapp"))
  .settings(
    commonSettings,
    name := "webapp",
    version := "0.0.1",
    libraryDependencies ++= Seq(
      AkkaStream,
      AkkaHttp
    )
  )

lazy val publisher = (project in file("publisher"))
  .settings(
    commonSettings,
    name := "publisher",
    version := "0.0.1",
    libraryDependencies ++= Seq(
      AkkaHttp,
      KafkaStream
    )
  )
