package org.startupDirectory.data

import play.api._
import play.api.mvc._
import play.api.db._
import play.api.Play.current
// import play.api.db.
// import javax.inject.Singleton
// import javax.inject.Inject
import java.sql.Timestamp
import scala.slick.driver.H2Driver
import scala.slick.session.Session
import scala.slick.session.Database
import play.api.libs.json.Json
import play.api.libs.json._
import com.typesafe.scalalogging.slf4j.Logging
import org.startupDirectory.util.Clock
import org.startupDirectory.util.TimestampFormatter
import java.util.Date

case class Etn(oasth: Date)

object Formatters {
  import TimestampFormatter._

  implicit val entityFormatter = Json.format[Entity]
  implicit val loginFormatter = Json.format[Login]
}

object EntityController extends Controller {

  import Formatters._
  
  val entityStore: EntityStore = new EntityStore(H2Driver, new Clock);
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
  def fbLogin = Action(parse.json) { request =>
    request.body.validate[Login].map { user =>
      database.withSession { implicit session: Session => 
        val id = entityStore.login(user)
        Ok
        // Ok(s"${id}").withSession(
        //   session + 
        //   ("user.id" -> "0") +
        //   ("user.email" -> "michele@gmail.com") +
        //   ("user.fbId" -> "833824640"))
      }
    }.recoverTotal {
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }
}
