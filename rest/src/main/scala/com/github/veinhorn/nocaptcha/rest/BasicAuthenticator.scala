package com.github.veinhorn.nocaptcha.rest

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.directives.{AuthenticationDirective, Credentials}
import com.github.veinhorn.nocaptcha.rest.models.repositories.UserRepository

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by VEINHORN on 15.05.2018.
  */
trait BasicAuthenticator extends Directives {
  def basicAuthenticator(credentials: Credentials)(implicit ec: ExecutionContext, userRepo: UserRepository): Future[Option[String]] = {
    credentials match {
      case p@Credentials.Provided(username) =>
        userRepo.findByUsername(username).map {
          case Some(user) if p.verify(user.password) => Some(username)
          case _ => None
        }
      case _ => Future.successful(None)
    }
  }

  def basicAuth(implicit ec: ExecutionContext, userRepo: UserRepository): AuthenticationDirective[String] =
    authenticateBasicAsync[String]("secure your service", basicAuthenticator)
}
