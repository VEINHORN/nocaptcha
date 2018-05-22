package com.github.veinhorn.nocaptcha.publisher

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.server.Directives._
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink}
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.util.Random

/**
  * Created by VEINHORN on 18.05.2018.
  */
object Main extends App {
  lazy val config = ConfigFactory.load

  implicit val system = ActorSystem("publisher")
  implicit val materializer = ActorMaterializer()

  def consumerStream(topic: String) =
    Consumer.atMostOnceSource(settings, Subscriptions.topics(topic))
            .map(record => TextMessage(record.value()))

  // Handle incoming WebSocket requests
  val handler =
    Flow[Message].mapConcat {
      case tm: TextMessage =>
        println("received text")
        TextMessage("some response") :: Nil
      case bm: BinaryMessage =>
        println("received binary")
        bm.dataStream.runWith(Sink.ignore)
        Nil
    }
    .merge(consumerStream("input_captchas"))

  val websocketRoute =
    path("ws") {
      handleWebSocketMessages(handler)
    }

  val bindingFuture =
    Http().bindAndHandle(websocketRoute, config.getString("app.host"), config.getInt("app.port"))

  def settings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
    .withBootstrapServers("localhost:9092")
    .withGroupId("publishers")
}
