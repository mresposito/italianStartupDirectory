package org.startupDirectory.pages

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(org.startupDirectory.pages.html.index())
  }

  // def persone = Action {
  //   Ok(org.startupDirectory.pages.html.persone())
  // }

  // def persone = Action {
  //   Ok(org.startupDirectory.pages.html.startup())
  // }

  // def risorse = Action {
  //   Ok(org.startupDirectory.pages.html.risorse())
  // }
  
  def privacy = Action {
    Ok(org.startupDirectory.pages.html.privacy())
  }

  def about = Action {
    Ok(org.startupDirectory.pages.html.about())
  }
}
