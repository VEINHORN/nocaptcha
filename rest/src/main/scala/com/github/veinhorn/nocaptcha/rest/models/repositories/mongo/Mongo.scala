package com.github.veinhorn.nocaptcha.rest.models.repositories.mongo

import com.github.veinhorn.nocaptcha.rest.models.User
import com.typesafe.config.ConfigFactory
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.{MongoClient, MongoCollection}
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY

/**
  * Created by VEINHORN on 14.05.2018.
  */
object Mongo {
  lazy val config = ConfigFactory.load
  lazy val mongoClient = MongoClient(config.getString("mongo.uri"))

  lazy val codecRegistry = fromRegistries(fromProviders(classOf[User]), DEFAULT_CODEC_REGISTRY)

  lazy val database = mongoClient.getDatabase(config.getString("mongo.database"))
                                 .withCodecRegistry(codecRegistry)

  lazy val userCollection: MongoCollection[User] = database.getCollection("users")
}
