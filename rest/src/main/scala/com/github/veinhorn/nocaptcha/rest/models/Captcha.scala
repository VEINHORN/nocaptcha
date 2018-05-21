package com.github.veinhorn.nocaptcha.rest.models

import java.util.Date

/**
  * Created by VEINHORN on 16.05.2018.
  */
object Captcha {
  def apply(data: String): Captcha = Captcha(data, new Date())
}

case class Captcha(data: String, created: Date)
