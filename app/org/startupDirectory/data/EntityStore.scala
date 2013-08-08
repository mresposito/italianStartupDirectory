package org.startupDirectory.data

import scala.slick.driver.ExtendedProfile
import java.sql.Timestamp


abstract class TimedEntity(login: Boolean = false, updated: Boolean = false, 
  created: Boolean = false) {

    var lastUpdated: Timestamp
    var lastCreated: Timestamp
    var dateJoined: Timestamp
}

case class Entity(id: Option[Long], name: String, email: String)
case class NameEmail(id: Option[Long], name: String, email: String)

class EntityStore (val driver: ExtendedProfile) {

  import driver.simple._

  object NameEmails extends Table[NameEmail]("entity") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")

    def * = id.? ~ name ~ email <> (NameEmail, NameEmail.unapply _)
    def autoInc = * returning id
  }

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

    def lastLogin = column[Timestamp]("last_login")
    def lastUpdated  = column[Timestamp]("last_updated")
    def dateJoined = column[Timestamp]("date_joined")

    def contactMe = column[Int]("contact_me")
    def emailMe = column[Int]("email_me")

    def * = id.? ~ name ~ email <> (Entity, Entity.unapply _)
    def autoInc = * returning id
  }

  def create(implicit session: Session) = (Entities.ddl).create
  def drop(implicit session: Session) = (Entities.ddl).drop

  def byEmail(email: String)(implicit session: Session) =
    Query(Entities).filter(_.email === email).firstOption

  def insertEntity(entity: Entity)(implicit session: Session): Long = 
    Entities.autoInc.insert(entity)

  def insertNameEmail(entity: NameEmail)(implicit session: Session): Long = 
    NameEmails.autoInc.insert(entity)

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
