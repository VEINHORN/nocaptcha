package com.github.veinhorn.nocaptcha.rest.api.v1

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.veinhorn.nocaptcha.rest.models.{Captcha, User}
import org.mongodb.scala.bson.ObjectId
import spray.json.{DefaultJsonProtocol, DeserializationException, JsObject, JsString, JsValue, RootJsonFormat}

/**
  * Created by VEINHORN on 14.05.2018.
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object UserJsonFormat extends RootJsonFormat[User] {
    override def write(user: User): JsValue = JsObject(
      "username" -> JsString(user.username),
      "email" -> JsString(user.email)
    )

    override def read(json: JsValue): User = {
      json.asJsObject.getFields("username", "email", "password", "id") match {
        case Seq(JsString(username), JsString(email), JsString(password), JsString(id)) =>
          User(username, email, password, Option(new ObjectId(id)))
        case Seq(JsString(username), JsString(email), JsString(password)) =>
          User(username, email, password)
        case _ => throw DeserializationException("incorrect user data")
      }
    }
  }

  implicit object CaptchaJsonFormat extends RootJsonFormat[Captcha] {
    override def write(captcha: Captcha): JsValue = JsObject()

    override def read(json: JsValue): Captcha = {
      json.asJsObject.getFields("data") match {
        case Seq(JsString(data)) => Captcha(data)
        case _ => throw DeserializationException("missing some of the captcha data")
      }
    }
  }

  implicit val errorJsonFormat = jsonFormat1(com.github.veinhorn.nocaptcha.rest.models.Error)
}
