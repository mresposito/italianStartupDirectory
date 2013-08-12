package org.startupDirectory.data

import java.sql.Timestamp

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

    def created = column[Timestamp]("created")
    def lastUpdated  = column[Timestamp]("last_updated")

    def contactMe = column[Int]("contact_me")
    def emailMe = column[Int]("email_me")

    def * = id.? ~ name ~ email <> (Entity, Entity.unapply _)
    def autoInc = * returning id
  }
}
