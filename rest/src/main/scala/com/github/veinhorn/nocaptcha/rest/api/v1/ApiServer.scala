package com.github.veinhorn.nocaptcha.rest.api.v1

import akka.actor.ActorRef
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{ExceptionHandler, HttpApp, Route}
import akka.http.scaladsl.settings.RoutingSettings
import com.github.veinhorn.nocaptcha.rest.api.v1.endpoints.{CaptchaEndpoint, UserEndpoint}
import com.github.veinhorn.nocaptcha.rest.core.producers.ProducerStreamManager.InitializeProducerStream
import com.github.veinhorn.nocaptcha.rest.core.producers.{DataProducer, ProducerStreamManager}
import com.github.veinhorn.nocaptcha.rest.exceptions.UserAlreadyExistException
import com.github.veinhorn.nocaptcha.rest.models.Error
import com.github.veinhorn.nocaptcha.rest.models.repositories.mongo.{Mongo, MongoUserRepository}

/**
  * Created by VEINHORN on 14.05.2018.
  */
object ApiServer extends HttpApp with JsonSupport {
  private var producerStreamManager: ActorRef = _
  private var dataProducer: ActorRef = _

  def exceptionHandler(implicit settings: RoutingSettings): ExceptionHandler = ExceptionHandler {
    case e: UserAlreadyExistException => complete(InternalServerError, Error(e.getMessage))
    case e: Exception => ExceptionHandler.default(settings).apply(e)
  }

  override def routes: Route =
    extractSettings { implicit settings =>
      handleExceptions(exceptionHandler) {
        pathPrefix("api" / "v1") {
          new UserEndpoint(new MongoUserRepository(Mongo.userCollection)).routes ~
          new CaptchaEndpoint(new MongoUserRepository(Mongo.userCollection), dataProducer).routes
        }
      }
    }

  override def postHttpBinding(binding: Http.ServerBinding): Unit = {
    super.postHttpBinding(binding)

    producerStreamManager = systemReference.get().actorOf(ProducerStreamManager.props, "ProducerStreamManager")
    dataProducer = systemReference.get().actorOf(DataProducer.props, "DataProducer")

    producerStreamManager ! InitializeProducerStream(dataProducer)
  }

}
