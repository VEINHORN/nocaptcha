package com.github.veinhorn.nocaptcha.rest.core.producers

import akka.actor.{Actor, ActorRef, Props}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Source}
import com.github.veinhorn.nocaptcha.rest.core.producers.EventMessages.ActivatedProducerStream
import com.github.veinhorn.nocaptcha.rest.core.producers.ProducerStreamManager.InitializeProducerStream
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

/**
  * Created by VEINHORN on 16.05.2018.
  */
object ProducerStreamManager {
  case class InitializeProducerStream(producerActorRef: ActorRef)

  def props: Props = Props(new ProducerStreamManager)
}

class ProducerStreamManager extends Actor {
  import com.github.veinhorn.nocaptcha.rest.api.v1.JsonSupport

  implicit val materializer = ActorMaterializer()

  override def receive: Receive = {
    case InitializeProducerStream(producer) =>
      producer ! ActivatedProducerStream(createStream[Any]("input_captchas"))
  }

  def createStream[T](topic: String) =
    Source.queue[T](Int.MaxValue, OverflowStrategy.backpressure)
    .map(_.toString/*obj: T => JsonSupport*/)
    .via(Flow[String].map { msg =>
      new ProducerRecord[Array[Byte], String](topic, msg)
    })
    .to(Producer.plainSink(settings))
    .run()

  def settings =
    ProducerSettings(context.system, new ByteArraySerializer, new StringSerializer)
      .withBootstrapServers("localhost:9092")
}
