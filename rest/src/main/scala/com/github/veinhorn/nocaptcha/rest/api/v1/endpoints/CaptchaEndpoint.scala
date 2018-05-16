package com.github.veinhorn.nocaptcha.rest.api.v1.endpoints

import akka.http.scaladsl.server.{Directives, Route}
import com.github.veinhorn.nocaptcha.rest.Endpoint
import com.github.veinhorn.nocaptcha.rest.models.Captcha

/**
  * Created by VEINHORN on 16.05.2018.
  */
class CaptchaEndpoint extends Endpoint {
  override def routes: Route =
    pathPrefix("captchas") {
      publishCaptcha
    }

  def publishCaptcha: Route =
    post {
      entity(as[Captcha]) { captcha =>
        extractExecutionContext { implicit ec =>
          onSuccess() {

          }
        }
      }
    }
}
