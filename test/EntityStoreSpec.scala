package org.startupDirectory.data

import org.mockito.Mockito._
import org.specs2.mutable._
import org.startupDirectory.utils.Clock
import play.api.test._
import play.api.test.Helpers._
import play.api.db._
import play.api.Play.current
import scala.slick.driver.H2Driver
import scala.slick.session.Session
import scala.slick.session.Database
import java.sql.Timestamp

abstract class  MockEntityStore extends Specification {

  val clock = mock(classOf[Clock])
  val store: EntityStore = new EntityStore(H2Driver, clock)
  val baseEntity = Entity(None, "michele", "m@e.com")
  val entityTwo = Entity(None, "tonino", "met@e.com")

  val baseLogin = Login("michele", "m@e.com", "facebook", "881133")
  val timestamp = new Timestamp(500)
}

class LoginStoreSpec extends MockEntityStore {
  "Login store" should {

    "insert new login" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val ret = store.insert(baseLogin)
          ret.toInt must beGreaterThan(0)
        }
      }
    }

    "insert and retrieve new login" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val id = store.insert(baseLogin)
          id.toInt must beGreaterThan(0)
 
          val newOpt = baseLogin.copy(id = Some(id))
          val retrieved = store.loginByEmail(baseLogin.email)
          retrieved.isDefined must beTrue
          retrieved.get must beEqualTo(newOpt)
        }
      }
    }

    "should be created with the right time" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>

          when(clock.now).thenReturn(timestamp)
          val id = store.login(baseLogin)
          id.toInt must beGreaterThan(0)
 
          val newOpt = baseLogin.copy(id = Some(id), created = Some(timestamp), lastLogin = Some(timestamp))
          val retrieved = store.loginByEmail(baseLogin.email)
          retrieved.isDefined must beTrue
          retrieved.get must beEqualTo(newOpt)
        }
      }
    }
  }
}

class EntityStoreSpec extends MockEntityStore {
  "Entity store" should {

    "insert new entity" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val id = store.insert(baseEntity)
          id.toInt must beGreaterThan(0)
        }
      }
    }

    "insert and retrieve new entity" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val id = store.insert(baseEntity)
          // retrieve the inserted
          val ret = store.byEmail(baseEntity.email)
          ret.isDefined must beTrue

          val correctId = baseEntity.copy(id = Some(id))
          correctId must beEqualTo(ret.get)
        }
      }
    }

    "get or create by email" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val id = store.getOrCreateByEmail(baseEntity)
          val secondId = store.getOrCreateByEmail(baseEntity)

          id must beEqualTo(secondId)
        }
      }
    }
  }
}
