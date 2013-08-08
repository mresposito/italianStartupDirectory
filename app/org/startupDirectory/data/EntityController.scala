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

  import Formatters._
  
  val entityStore: EntityStore = new EntityStore(H2Driver);
  val database: Database = new Database {
    override def createConnection() = DB.getConnection()
  }

  def get = Action {
    val insert = database.withSession { implicit session: Session => 
      // entityStore.insertEntity(
      //   Entity(None, "b", "e", "ich")
      // )
    }
    Ok
  }

  /**
   * Login the person, and create a new instance in the DB
   */
  def fbLogin = Action { request =>
    Ok
    // request.body.validate[Entity].map {
    //   case req => {
    // database.withSession { implicit session: Session => 
    //   val id = entityStore.getOrCreate(entity)
    //   Ok(id).withSession(
    //     session + 
    //     ("user.id" -> "0") +
    //     ("user.email" -> "michele@gmail.com") +
    //     ("user.fbId" -> "833824640"))
    // }
      // }
    // }.recoverTotal{
    //   e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    // }
  }
}
