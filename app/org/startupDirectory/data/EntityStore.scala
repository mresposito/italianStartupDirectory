package org.startupDirectory.data

import scala.slick.driver.ExtendedProfile
import java.sql.Timestamp
import org.startupDirectory.utils.Clock

trait Profile {
  val profile: ExtendedProfile
}

case class Login(name: String, email: String, loginType: String,
  loginSecret: String, lastLogin: Timestamp, dateJoined: Timestamp, id: Option[Long] = None)

trait LoginComponent { this: Profile =>
  import profile.simple._

  object Logins extends Table[Login]("login") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")

    def loginType = column[String]("login_type")
    def loginSecret = column[String]("login_secret")

    def dateJoined = column[Timestamp]("date_joined")
    def lastLogin = column[Timestamp]("last_login")

    def * = name ~ email ~
      loginType ~ loginSecret ~ lastLogin ~
      dateJoined ~ id.? <> (Login, Login.unapply _)

    def autoInc = * returning id
  }
}

case class Entity(id: Option[Long], name: String, email: String)

trait EntityComponent { this: Profile =>
  import profile.simple._

  object Entities extends Table[Entity]("entity") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")
    def description = column[Option[String]]("email")

    /* index */
    def idx = index("idx_id", id, unique=true)
    def workStatus = column[Int]("work_status") // -1
    def size = column[Int]("size")
    def age = column[Int]("age")

    def isActive = column[Int]("is_active")
    def isStaff = column[Int]("is_staff")

    /* social */
    def webPage = column[Option[String]]("web_page")
    def github = column[Option[String]]("github_url")
    def fb = column[Option[String]]("fb_url")
    def fbId = column[Option[String]]("fb_id")
    def gPlus = column[Option[String]]("g_plus_url")

    def dateJoined = column[Timestamp]("date_joined")
    def lastUpdated  = column[Timestamp]("last_updated")

    def contactMe = column[Int]("contact_me")
    def emailMe = column[Int]("email_me")

    def * = id.? ~ name ~ email <> (Entity, Entity.unapply _)
    def autoInc = * returning id
  }
}

/**
* The Data Access Layer contains all components and a profile
*/
class EntityStore(override val profile: ExtendedProfile, val clock: Clock) extends EntityComponent with LoginComponent with Profile {
  import profile.simple._

  val allDdl = Entities.ddl ++ Logins.ddl

  def create(implicit session: Session): Unit = {
    (allDdl).create //helper method to create all tables
  }
  def drop(implicit session: Session): Unit  = {
    (allDdl).drop
  }

  def byEmail(email: String)(implicit session: Session) =
    Query(Entities).filter(_.email === email).firstOption

  def insertEntity(entity: Entity)(implicit session: Session): Long = {
    Entities.autoInc.insert(entity)
  }

  // def insert(login: Login)(implicit session: Session): Long = {
  //   val withTime = login.
  // }

  def getOrCreateByEmail(entity: Entity)(implicit session: Session): Long = {
    getOrCreateByFun(entity, entity.email, byEmail)
  }

  def getOrCreateByFun(entity: Entity, property:String, find: String => Option[Entity])
    (implicit session:Session): Long = {

    val found = find(property)
    if(found.isDefined) {
      found.get.id.get
    } else {
      insertEntity(entity)
    }
  }
}
