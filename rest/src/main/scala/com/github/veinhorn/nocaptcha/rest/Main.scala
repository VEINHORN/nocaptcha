package com.github.veinhorn.nocaptcha.rest

import com.github.veinhorn.nocaptcha.rest.api.v1.ApiServer
import com.typesafe.config.ConfigFactory

/**
  * Created by VEINHORN on 14.05.2018.
  */
object Main extends App {
  val config = ConfigFactory.load
  ApiServer.startServer(config.getString("app.host"), config.getInt("app.port"))
}
