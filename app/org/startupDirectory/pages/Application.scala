package org.startupDirectory.pages

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(org.startupDirectory.pages.html.index("Your new application is ready."))
  }
  
}
