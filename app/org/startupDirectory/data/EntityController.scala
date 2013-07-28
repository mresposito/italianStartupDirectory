package org.startupDirectory.data

import play.api._
import play.api.mvc._
// import javax.inject.Singleton
// import javax.inject.Inject
import play.api.libs.json.Json
import scala.slick.driver.H2Driver
import play.api.libs.json._
import com.typesafe.scalalogging.slf4j.Logging

object Formatters {
  implicit val entityFormatter = Json.format[Entity]
}

class EntityController extends Controller{
  
  val entityStore: EntityStore = new EntityStore(H2Driver);

  def get = Action {
    Ok
  }
}
