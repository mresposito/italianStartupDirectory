package org.startupDirectory.data

import play.api._
import play.api.mvc._
import javax.inject.Singleton
import javax.inject.Inject
import java.sql.Timestamp
import scala.slick.session.Session
import scala.slick.session.Database
import play.api.libs.json.Json
import play.api.libs.json._
import com.typesafe.scalalogging.slf4j.Logging
import org.startupDirectory.util.Clock
import org.startupDirectory.util.TimestampFormatter
import org.startupDirectory.common.SessionManaged
import java.util.Date

object StoreFormatters {
  import TimestampFormatter._

  implicit val entityFormatter = Json.format[Entity]
  implicit val loginFormatter = Json.format[Login]
}

@Singleton
class EntityController @Inject()(entityStore: DAL, databaseConn: DatabaseConnection, sessionManager: SessionManaged) extends Controller {

  import StoreFormatters._
  import databaseConn.database

  def get = Action {
    Ok
  }

  /**
   * Login the person, and create a new instance in the DB
   */
  def fbLogin = Action(parse.json) { request =>
    request.body.validate[Login].map { user =>
      database.withSession { implicit session: Session => 

        val id = entityStore.login(user)
        val userWithId = user.copy(id = Some(id))
        val response = Ok(Json.toJson(userWithId))
        sessionManager.addSession(response, userWithId)(request)
      }
    }.recoverTotal {
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }
}
