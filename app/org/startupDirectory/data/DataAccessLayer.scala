package org.startupDirectory.data

import scala.slick.driver.H2Driver
import javax.inject.Singleton
import javax.inject.Inject
import org.startupDirectory.util.Clock
import scala.slick.driver.ExtendedProfile

trait Profile {
  val profile: ExtendedProfile
}

class H2Profile extends Profile {
  val profile = H2Driver
}

/**
* The Data Access Layer contains all components and a profile
*/
@Singleton
class DAL @Inject() (val profileCake: Profile, val clock: Clock) extends EntityComponent with LoginComponent with Profile {
  override val profile = profileCake.profile
  import profile.simple._

  val allDdl = Entities.ddl ++ Logins.ddl

  def create(implicit session: Session): Unit = {
    (allDdl).create //helper method to create all tables
  }

  def drop(implicit session: Session): Unit = {
    (allDdl).drop
  }

  def login(log: Login)(implicit session: Session): Long = {
    val found = getLoginByEmail(log.email) 
    if(found.isDefined) { // update login time
      updateLoginTime(found.get)
      found.get.id.get
    } else {
      val now = Some(clock.now)
      val updateTimestamps = log.copy(created = now, lastLogin = now)
      insert(updateTimestamps)
    }
  }

  def updateLoginTime(login: Login)(implicit session: Session): Unit = {
    Query(Logins).filter(_.id === login.id.get).map(_.lastLogin).update(Some(clock.now))
  }

  def find(login: Login)(implicit session: Session) = {
    if(login.id.isDefined) {
      Query(Logins).filter(_.id === login.id.get).firstOption
    } else {
      None
    }
  }

  def byEmail(email: String)(implicit session: Session) =
    Query(Entities).filter(_.email === email).firstOption
  
  def getLoginByEmail(email: String)(implicit session: Session) = {
    Query(Logins).filter(_.email === email).firstOption
  }

  def insert(entity: Entity)(implicit session: Session): Long = {
    Entities.autoInc.insert(entity)
  }

  def insert(login: Login)(implicit session: Session): Long = {
    Logins.autoInc.insert(login)
  }

  def getOrCreateByEmail(entity: Entity)(implicit session: Session): Long = {
    getOrCreateByFun(entity, entity.email, byEmail)
  }

  def getOrCreateByFun(entity: Entity, property:String, find: String => Option[Entity])
    (implicit session:Session): Long = {

    val found = find(property)
    if(found.isDefined) {
      found.get.id.get
    } else {
      insert(entity)
    }
  }
}
