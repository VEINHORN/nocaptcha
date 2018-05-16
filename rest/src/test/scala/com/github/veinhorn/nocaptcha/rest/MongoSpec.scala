package com.github.veinhorn.nocaptcha.rest

import com.github.veinhorn.nocaptcha.rest.models.User
import com.github.veinhorn.nocaptcha.rest.models.repositories.mongo.{Mongo, MongoUserRepository}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Updates
import org.scalatest.FlatSpec
import spray.json.{JsObject, JsString}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by VEINHORN on 14.05.2018.
  */
class MongoSpec extends FlatSpec {
  it should "test mongodb connection" in {
    val userRepo = new MongoUserRepository(Mongo.userCollection)
    val users = Await.result(userRepo.all, Duration.Inf)

    val user = User("test2", "test2@test2.test2", "qwerty2", Option(new ObjectId("5afae1d1547f835440ddfb36")))
    val u1 = Await.result(userRepo.update("5afae1d1547f835440ddfb36", user), Duration.Inf)

    val test = "test"

  }
}
