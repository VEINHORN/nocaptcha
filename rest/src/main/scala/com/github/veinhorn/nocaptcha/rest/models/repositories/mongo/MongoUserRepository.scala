package com.github.veinhorn.nocaptcha.rest.models.repositories.mongo

import com.github.veinhorn.nocaptcha.rest.exceptions.UserAlreadyExistException
import com.github.veinhorn.nocaptcha.rest.models.User
import com.github.veinhorn.nocaptcha.rest.models.repositories.UserRepository
import org.bson.types.ObjectId
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.{Filters, Updates}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by VEINHORN on 14.05.2018.
  */
class MongoUserRepository(collection: MongoCollection[User]) extends UserRepository {

  def save(user: User)(implicit ec: ExecutionContext): Future[UserId] = {
    def withId(user: User) = user.copy(_id = Option(ObjectId.get()))

    collection.count(Filters.eq("username", user.username)).head.zip(
      collection.count(Filters.eq("email", user.email)).head
    ) flatMap {
      case (0, 0) =>
        val newUser = withId(user)
        collection.insertOne(newUser).head.map(_ => newUser._id.map(_.toHexString).get)
      case _      => throw UserAlreadyExistException("user with such username or email already exists")
    }
  }

  override def all: Future[Seq[User]] = collection.find.toFuture

  override def findByUsername(username: String): Future[Option[User]] =
    collection.find(Filters.eq("username", username)).headOption

  override def update(id: String, user: User)(implicit ec: ExecutionContext): Future[UserId] = {
    def update = Updates.combine(
      Updates.set("username", user.username),
      Updates.set("email", user.email)
    )

    collection.findOneAndUpdate(Filters.eq[Object]("_id", new ObjectId(id)), update).head.map(_._id.map(_.toHexString).get)
  }

}
