package com.github.veinhorn.nocaptcha.rest.models

import java.util.{Date, UUID}

/**
  * Created by VEINHORN on 16.05.2018.
  */
object Captcha {
  val DataLengthLimit = 50

  def apply(publisherId: String, data: String): Captcha = Captcha(publisherId, data, new Date())
}

/**
  *
  * @param data is an image in Base64 data uri format
  * @param created date of captcha
  * @param key is the unique key for this captcha
  */
case class Captcha(publisherId: String,
                   data: String,
                   created: Date,
                   key: String = UUID.randomUUID().toString) {
  import Captcha.DataLengthLimit

  override def toString = s"Captcha(publisherId: $publisherId, data: ${data.take(DataLengthLimit)}..., created: $created, key: $key)"
}
