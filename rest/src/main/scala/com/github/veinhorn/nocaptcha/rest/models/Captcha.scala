package com.github.veinhorn.nocaptcha.rest.models

import java.util.{Date, UUID}

/**
  * Created by VEINHORN on 16.05.2018.
  */
object Captcha {
  def apply(data: String): Captcha = Captcha(data, new Date())
}

/**
  *
  * @param data is an image in Base64 data uri format
  * @param created date of captcha
  * @param key is the unique key for this captcha
  */
case class Captcha(data: String, created: Date, key: String = UUID.randomUUID().toString)
