package com.github.veinhorn.nocaptcha.publisher

import akka.actor.{Actor, Props}

/**
  * Created by VEINHORN on 21.05.2018.
  */
object Resolver {
  def props: Props = Props(new Resolver)
}

class Resolver extends Actor {
  override def receive = {
    case _ => println("")
  }
}
