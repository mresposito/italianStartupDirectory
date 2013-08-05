package org.startupDirectory.data

import play.api._
import play.api.mvc._
import play.api.db._
import play.api.Play.current
// import play.api.db.
// import javax.inject.Singleton
// import javax.inject.Inject
import scala.slick.session.Session
import scala.slick.session.Database
import play.api.libs.json.Json
import scala.slick.driver.H2Driver
import play.api.libs.json._
import com.typesafe.scalalogging.slf4j.Logging

object Formatters {
  implicit val entityFormatter = Json.format[Entity]
}

object EntityController extends Controller {
  
  val entityStore: EntityStore = new EntityStore(H2Driver);
  val database: Database = new Database {
    override def createConnection() = DB.getConnection()
  }

  def get = Action {
    val insert = database.withSession { implicit session: Session => 
      entityStore.insertEntity(
        Entity(None, "b", "e", "ich")
      )
    }
    Ok
  }
}
