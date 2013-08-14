package org.startupDirectory.pages

import play.api._
import play.api.mvc._
import javax.inject.Singleton
import javax.inject.Inject
import org.startupDirectory.data.Login
import org.startupDirectory.common.SessionManaged

@Singleton
class Application @Inject()(sessionManager: SessionManaged) extends Controller {
  
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

  def logout = Action { implicit request =>
    sessionManager.removeSession(Ok)
  }

  def privacy = Action {
    Ok(org.startupDirectory.pages.html.privacy())
  }

  def about = Action {
    Ok(org.startupDirectory.pages.html.about())
  }

  /**
   * TODO: Must be authenticated
   */
  def createEntity = Action { request =>
    val loggedIn = request.session.get("user.id").isDefined
    Ok(org.startupDirectory.pages.create.html.index())
  }

  def createTemplates(name: String) = Action { request =>
    if(name == "persona") {
      Ok(org.startupDirectory.pages.create.html.persona())
    } else {
      Ok
    }
  }
}
