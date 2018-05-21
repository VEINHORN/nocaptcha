package com.github.veinhorn.nocaptcha.rest.api.v1.endpoints

import akka.event.LoggingAdapter
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route}
import com.github.veinhorn.nocaptcha.rest.BasicAuthenticator
import com.github.veinhorn.nocaptcha.rest.api.v1.JsonSupport
import com.github.veinhorn.nocaptcha.rest.models.User
import com.github.veinhorn.nocaptcha.rest.models.repositories.UserRepository
import spray.json.{JsObject, JsString}

/**
  * Created by VEINHORN on 14.05.2018.
  */
class UserEndpoint(userRepo: UserRepository) extends Directives with BasicAuthenticator with JsonSupport {
  def routes: Route =
    pathPrefix("users") {
      extractLog { implicit log =>
        getUser ~ getUsers ~ createUser ~ updateUser
      }
    }

  def getUser(implicit log: LoggingAdapter): Route =
    (get & path(Segment)) { username =>
      onSuccess(userRepo.findByUsername(username)) {
        case Some(user) => complete(user)
        case None       => log.info(s"Cannot find user with username=$username"); complete(NotFound)
      }
    }

  def getUsers: Route =
    (get & onComplete(userRepo.all)) { users =>
      complete(users)
    }

  def createUser(implicit log: LoggingAdapter): Route =
    post {
      entity(as[User]) { user =>
        log.info(s"user=$user")

        extractExecutionContext { implicit ec =>
          onSuccess(userRepo.save(user)) { userId =>
            log.info(s"$user was successfully stored in repository")
            complete(Created, JsObject("userId" -> JsString(userId)))
          }
        }
      }
    }

  def updateUser: Route =
    put {
      extractExecutionContext { implicit ec =>
        basicAuth(ec, userRepo) { username =>
          entity(as[User]) {
            case user@User(username, email, password, Some(id)) =>
              onSuccess(userRepo.update(id.toHexString, user)) { userId =>
                complete(OK, JsObject("userId" -> JsString(userId)))
              }
            case _ => complete(BadRequest)
          }
        }
      }
    }
}
