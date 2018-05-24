package com.github.veinhorn.nocaptcha.webapp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
/**
  * Created by VEINHORN on 18.05.2018.
  */
object Main extends App {
  lazy val config = ConfigFactory.load

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val routes = get {
    path(Segment) { resource =>
      getFromResource(resource)
    } ~
    getFromResource("index.html", ContentTypes.`text/html(UTF-8)`)
  }

  Http().bindAndHandle(routes, config.getString("app.host"), config.getInt("app.port"))
}
