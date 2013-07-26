package org.startupDirectory.data

import scala.slick.driver.ExtendedProfile
import java.sql.Timestamp

case class Entity(id: Long, name: String, surname: String) 

class EntityStore (val driver: ExtendedProfile) {

  import driver.simple._

  object Enities extends Table[Entity]("entity") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def surname = column[String]("surname")
    def email = column[Option[String]]("email")
    def description = column[Option[String]]("email")
    def workStatus = column[Int]("work_status") // -1

    def isActive = column[Int]("is_active") == 1
    def isStaff = column[Int]("is_staff") == 1

    def webPage = column[Option[String]]("web_page")
    def github = column[Option[String]]("github_url")
    def fb = column[Option[String]]("fb_url")
    def gPlus = column[Option[String]]("g_plus_url")

    def lastLogin = column[Timestamp]("last_login")
    def lastUpdated  = column[Timestamp]("last_updated")
    def dateJoined = column[Timestamp]("date_joined")

    def contactMe = column[Int]("contact_me") == 1
    def emailMe = column[Int]("email_me") == 1

    def * = id ~ name ~ surname <> (Entity, Entity.unapply _)
  }
}
