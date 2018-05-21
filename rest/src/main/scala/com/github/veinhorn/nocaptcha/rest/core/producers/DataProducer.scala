package com.github.veinhorn.nocaptcha.rest.core.producers

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import akka.stream.scaladsl.SourceQueueWithComplete
import com.github.veinhorn.nocaptcha.rest.core.producers.EventMessages.{ActivatedProducerStream, MessagePublished}
import com.github.veinhorn.nocaptcha.rest.models.Captcha

/**
  * Created by VEINHORN on 16.05.2018.
  */
object DataProducer {
  case class PublishMessage(captcha: Captcha)

  def props: Props = Props(new DataProducer)
}

class DataProducer extends Actor with ActorLogging {
  import DataProducer._

  private var producerStream: SourceQueueWithComplete[Any] = _

  import context.dispatcher

  override def receive: Receive = {
    case ActivatedProducerStream(producer) =>
      producerStream = producer
      context.become(publishMode)
    case other => log.error(s"unsupported message $other")
  }

  private def publishMode: Receive = {
    case PublishMessage(captcha) => producerStream.offer(captcha).map(_ => MessagePublished) pipeTo sender()
    case other => log.error(s"unsupported message $other")
  }
}
