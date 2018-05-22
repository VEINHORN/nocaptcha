package com.github.veinhorn.nocaptcha.rest.core.producers

import akka.stream.scaladsl.SourceQueueWithComplete

/**
  * Created by VEINHORN on 16.05.2018.
  */
object EventMessages {
  trait EventMessage

  case class ActivatedProducerStream[T](producerStream: SourceQueueWithComplete[T]) extends EventMessage

  /**
    * Event which means that captcha was successfully published to the Kafka
    * @param key is the unique key of captcha
    */
  case class CaptchaPublished(key: String) extends EventMessage
}
