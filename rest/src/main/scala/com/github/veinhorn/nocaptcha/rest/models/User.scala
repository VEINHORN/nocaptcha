package com.github.veinhorn.nocaptcha.rest.models

import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId

/**
  * Created by VEINHORN on 14.05.2018.
  */
case class User(username: String,
                email: String,
                password: String,
                _id: Option[ObjectId] = None)
