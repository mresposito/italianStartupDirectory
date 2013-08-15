package org.startupDirectory.data

import scala.slick.driver.H2Driver
import javax.inject.Singleton
import javax.inject.Inject
import org.startupDirectory.util.Clock
import scala.slick.driver.ExtendedProfile

trait SlickProfile {
  val profile: ExtendedProfile
}

class H2Profile extends SlickProfile {
  val profile = H2Driver
}

/**
* The Data Access Layer contains all components and a profile
*/
@Singleton
class DAL @Inject() (val profileCake: SlickProfile, val clock: Clock) extends ProfileComponent with UserComponent with SlickProfile {
  override val profile = profileCake.profile
  import profile.simple._

  val allDdl = Profiles.ddl ++ Users.ddl

  def create(implicit session: Session): Unit = {
    (allDdl).create //helper method to create all tables
  }

  def drop(implicit session: Session): Unit = {
    (allDdl).drop
  }

  def login(user: User)(implicit session: Session): Long = {
    val found = getUserByEmail(user.email) 
    if(found.isDefined) { // update login time
      updateLoginTime(found.get)
      found.get.id.get
    } else {
      val now = Some(clock.now)
      val updateTimestamps = user.copy(created = now, lastLogin = now)
      insert(updateTimestamps)
    }
  }

  def updateLoginTime(user: User)(implicit session: Session): Unit = {
    Query(Users).filter(_.id === user.id.get).map(_.lastLogin).update(Some(clock.now))
  }

  def find(user: User)(implicit session: Session) = {
    if(user.id.isDefined) {
      Query(Users).filter(_.id === user.id.get).firstOption
    } else {
      None
    }
  }

  def byEmail(email: String)(implicit session: Session) =
    Query(Profiles).filter(_.email === email).firstOption
  
  def getUserByEmail(email: String)(implicit session: Session) = {
    Query(Users).filter(_.email === email).firstOption
  }

  def insert(profile: Profile)(implicit session: Session): Long = {
    Profiles.autoInc.insert(profile)
  }

  def insert(user: User)(implicit session: Session): Long = {
    Users.autoInc.insert(user)
  }

  def getOrCreateByEmail(profile: Profile)(implicit session: Session): Long = {
    getOrCreateByFun(profile, profile.email, byEmail)
  }

  def getOrCreateByFun(profile: Profile, property:String, find: String => Option[Profile])
    (implicit session:Session): Long = {

    val found = find(property)
    if(found.isDefined) {
      found.get.id.get
    } else {
      insert(profile)
    }
  }
}
