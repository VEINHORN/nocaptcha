package com.github.veinhorn.nocaptcha.rest.api.v1

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{ExceptionHandler, HttpApp, Route}
import akka.http.scaladsl.settings.RoutingSettings
import com.github.veinhorn.nocaptcha.rest.api.v1.endpoints.UserEndpoint
import com.github.veinhorn.nocaptcha.rest.exceptions.UserAlreadyExistException
import com.github.veinhorn.nocaptcha.rest.models.Error
import com.github.veinhorn.nocaptcha.rest.models.repositories.mongo.{Mongo, MongoUserRepository}

/**
  * Created by VEINHORN on 14.05.2018.
  */
object ApiServer extends HttpApp with JsonSupport {


  def exceptionHandler(implicit settings: RoutingSettings): ExceptionHandler = ExceptionHandler {
    case e: UserAlreadyExistException => complete(InternalServerError, Error(e.getMessage))
    case e: Exception => ExceptionHandler.default(settings).apply(e)
  }

  override def routes: Route =
    extractSettings { implicit settings =>
      handleExceptions(exceptionHandler) {
        pathPrefix("api" / "v1") {
          new UserEndpoint(new MongoUserRepository(Mongo.userCollection)).routes
        }
      }
    }

}
