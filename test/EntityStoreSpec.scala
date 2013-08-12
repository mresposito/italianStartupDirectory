package org.startupDirectory.data

import org.mockito.Mockito.{mock, when}
import org.specs2.mutable._
import org.startupDirectory.util.Clock
import play.api.test._
import play.api.test.Helpers._
import play.api.db._
import play.api.Play.current
import scala.slick.session.Session
import scala.slick.session.Database
import java.sql.Timestamp

abstract class MockDAL extends Specification {

  val clock = mock(classOf[Clock])
  val store: DAL = new DAL(new H2Profile, clock)
  val baseEntity = Entity(None, "michele", "m@e.com")
  val entityTwo = Entity(None, "tonino", "met@e.com")

  val baseLogin = Login("michele", "m@e.com", "facebook", "881133")
  val timestamp = new Timestamp(500)
}

class EntityStoreSpec extends MockDAL {
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
