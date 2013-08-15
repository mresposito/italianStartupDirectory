package org.startupDirectory.common

import play.api._
import play.api.mvc._
import org.startupDirectory.data.User

trait SessionManaged {

  val userId = "user.id"
  val userEmail = "user.email"
  val userLoginSecret = "user.loginSecret"

  def addSession[A](result: Result, user: User)(implicit request: Request[A]): Result
  def removeSession[A](result: Result)(implicit request: Request[A]): Result
}

class SessionManager extends SessionManaged {
  
  def addSession[A](result: Result, user: User)(implicit request: Request[A]): Result = {
    result.withSession(
      request.session +
        (userId, s"${user.id.get}") +
        (userEmail, user.email) +
        (userLoginSecret, user.loginSecret))
  }

  def removeSession[A](result: Result)(implicit request: Request[A]): Result = {
    result.withSession(
      request.session - 
        userId - 
        userEmail -
        userLoginSecret)
  }
}

class MockSessionManager extends SessionManaged {
  
  def addSession[A](result: Result, user: User)(implicit request: Request[A]): Result = result
  def removeSession[A](result: Result)(implicit request: Request[A]): Result = result
}
