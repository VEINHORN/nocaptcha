package com.github.veinhorn.nocaptcha.rest

import akka.http.scaladsl.server.{Directives, Route}

/**
  * Created by VEINHORN on 16.05.2018.
  */
trait Endpoint extends Directives {
  def routes: Route
}
