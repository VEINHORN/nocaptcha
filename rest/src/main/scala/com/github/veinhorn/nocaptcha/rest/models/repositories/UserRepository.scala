package com.github.veinhorn.nocaptcha.rest.models.repositories

import com.github.veinhorn.nocaptcha.rest.models.User

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by VEINHORN on 14.05.2018.
  */
trait UserRepository {
  type UserId = String

  def save(user: User)(implicit ec: ExecutionContext): Future[UserId]
  def all: Future[Seq[User]]
  def findByUsername(username: String): Future[Option[User]]
  def update(id: String, user: User)(implicit ec: ExecutionContext): Future[UserId]
}
