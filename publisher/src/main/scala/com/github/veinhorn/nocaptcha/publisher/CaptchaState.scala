package com.github.veinhorn.nocaptcha.publisher

import akka.actor.{Actor, Props}
import akka.persistence.{Persistence, PersistentActor}

/**
  * Created by VEINHORN on 21.05.2018.
  */
object CaptchaState {
  def props: Props = Props(new CaptchaState)
}

class CaptchaState extends PersistentActor {
  override def receiveRecover: Receive = ???

  override def receiveCommand: Receive = ???

  override def persistenceId: String = ???
}
