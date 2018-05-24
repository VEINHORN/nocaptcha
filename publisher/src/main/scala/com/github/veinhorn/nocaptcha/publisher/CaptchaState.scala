package com.github.veinhorn.nocaptcha.publisher

import akka.actor.{Actor, Props}

/**
  * Created by VEINHORN on 21.05.2018.
  */
object CaptchaState {
  def props: Props = Props(new CaptchaState)
}

class CaptchaState extends Actor {
  override def receive = {
    case _ => println("")
  }
}
