package org.startupDirectory.pages

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action { request =>
    request.session.get("user.id").map { id =>
      Ok(org.startupDirectory.pages.html.index())
    }.getOrElse {
      Ok(org.startupDirectory.pages.html.welcome())
    }
  }

  def welcome = Action { request =>
    val loggedIn = request.session.get("user.id").isDefined
    Ok(org.startupDirectory.pages.html.welcome(loggedIn))
  }

  def search = Action { request =>
    val loggedIn = request.session.get("user.id").isDefined
    Ok(org.startupDirectory.pages.html.search(loggedIn))
  }

  def login = Action { implicit request =>
    Ok.withSession(
      session + 
        ("user.id" -> "0") +
        ("user.email" -> "michele@gmail.com") +
        ("user.fbId" -> "833824640"))
  }

  def logout = Action { implicit request =>
    Ok.withSession(
      session - 
        "user.id" - 
        "user.email" -
        "user.fbId"
    )
  }

  def privacy = Action {
    Ok(org.startupDirectory.pages.html.privacy())
  }

  def about = Action {
    Ok(org.startupDirectory.pages.html.about())
  }
}
