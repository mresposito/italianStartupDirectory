package org.startupDirectory.data

import java.sql.Timestamp

case class Login(name: String,
  email: String,
  loginType: String,
  loginSecret: String,
  lastLogin: Option[Timestamp] = None,
  created: Option[Timestamp] = None,
  id: Option[Long] = None)

trait LoginComponent { this: Profile =>
  import profile.simple._

  object Logins extends Table[Login]("login") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")

    def loginType = column[String]("login_type")
    def loginSecret = column[String]("login_secret")

    def lastLogin = column[Option[Timestamp]]("last_login")
    def created = column[Option[Timestamp]]("created")

    def * = name ~ email ~
      loginType ~ loginSecret ~ lastLogin ~
      created ~ id.? <> (Login, Login.unapply _)

    def autoInc = * returning id
  }
}
