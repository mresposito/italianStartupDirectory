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
  val baseProfile = Profile(None, "michele", "m@e.com")
  val entityTwo = Profile(None, "tonino", "met@e.com")

  val baseUser = User("michele", "m@e.com", "facebook", "881133")
  val timestamp = new Timestamp(500)
}

class ProfileStoreSpec extends MockDAL {
  "Profile store" should {

    "insert new entity" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val id = store.insert(baseProfile)
          id.toInt must beGreaterThan(0)
        }
      }
    }

    "insert and retrieve new entity" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val id = store.insert(baseProfile)
          // retrieve the inserted
          val ret = store.byEmail(baseProfile.email)
          ret.isDefined must beTrue

          val correctId = baseProfile.copy(id = Some(id))
          correctId must beEqualTo(ret.get)
        }
      }
    }

    "get or create by email" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val id = store.getOrCreateByEmail(baseProfile)
          val secondId = store.getOrCreateByEmail(baseProfile)

          id must beEqualTo(secondId)
        }
      }
    }
  }
}
