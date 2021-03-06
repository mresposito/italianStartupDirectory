package org.startupDirectory.data

import java.sql.Timestamp

case class User(name: String,
  email: String,
  loginType: String,
  loginSecret: String,
  lastLogin: Option[Timestamp] = None,
  created: Option[Timestamp] = None,
  id: Option[Long] = None) {
  /**
   * a new user is defined if 
   * it has just been created
   */
  def newUser: Boolean = {
    if(lastLogin.isDefined && created.isDefined) {
      lastLogin.get == created.get
    } else {
      false
    }
  }
}

trait UserComponent { this: SlickProfile =>
  import profile.simple._

  object Users extends Table[User]("user") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")

    def loginType = column[String]("login_type")
    def loginSecret = column[String]("login_secret")

    def lastLogin = column[Option[Timestamp]]("last_login")
    def created = column[Option[Timestamp]]("created")

    def * = name ~ email ~
      loginType ~ loginSecret ~ lastLogin ~
      created ~ id.? <> (User, User.unapply _)

    def autoInc = * returning id
  }
}
