package com.github.veinhorn.nocaptcha.rest.api.v1.endpoints

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.github.veinhorn.nocaptcha.rest.SecureEndpoint
import com.github.veinhorn.nocaptcha.rest.api.v1.JsonSupport
import com.github.veinhorn.nocaptcha.rest.core.producers.DataProducer.PublishMessage
import com.github.veinhorn.nocaptcha.rest.core.producers.EventMessages.CaptchaPublished
import com.github.veinhorn.nocaptcha.rest.models.Captcha
import com.github.veinhorn.nocaptcha.rest.models.repositories.UserRepository
import spray.json.{JsObject, JsString}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by VEINHORN on 16.05.2018.
  */
class CaptchaEndpoint(userRepo: UserRepository, dataProducer: ActorRef) extends SecureEndpoint with JsonSupport {
  implicit val timeout = Timeout(10 seconds)

  override def routes: Route =
    pathPrefix("captchas") {
      extractExecutionContext { implicit ec =>
        basicAuth(ec, userRepo) { username =>
          publishCaptcha
        }
      }
    }

  def publishCaptcha(implicit ec: ExecutionContext): Route =
    post {
      entity(as[Captcha]) { captcha =>
        onSuccess(dataProducer ? PublishMessage(captcha)) {
          case CaptchaPublished(key) =>
            complete(Created, JsObject("captchaId" -> JsString(key)))
          case _ => complete(InternalServerError)
        }
      }
    }
}
