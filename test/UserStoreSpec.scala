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

class UserStoreSpec extends MockDAL {
  "User store" should {

    "insert new user" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val ret = store.insert(baseUser)
          ret.toInt must beGreaterThan(0)
        }
      }
    }

    "insert and retrieve new user" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val id = store.insert(baseUser)
          id.toInt must beGreaterThan(0)
 
          val newOpt = baseUser.copy(id = Some(id))
          val retrieved = store.getUserByEmail(baseUser.email)
          retrieved.isDefined must beTrue
          retrieved.get must beEqualTo(newOpt)
        }
      }
    }

    "create with the right time" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>

          when(clock.now).thenReturn(timestamp)
          val id = store.login(baseUser)
          id.toInt must beGreaterThan(0)
 
          val newOpt = baseUser.copy(id = Some(id), created = Some(timestamp), lastLogin = Some(timestamp))
          val retrieved = store.getUserByEmail(baseUser.email)
          retrieved.isDefined must beTrue
          retrieved.get must beEqualTo(newOpt)
        }
      }
    }

    "login with the right time" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val shortTimestamp = Some(new Timestamp(1))
          when(clock.now).thenReturn(shortTimestamp.get)
          // insert with timestamp
          val userWithTimestamp = baseUser.copy(created = shortTimestamp)
          val insertId = store.insert(userWithTimestamp)
          // make new login
          when(clock.now).thenReturn(timestamp)
          val id = store.login(baseUser)
          id.toInt must beGreaterThan(0)
          // retrieve login 
          val retrieved = store.getUserByEmail(baseUser.email)
          val correctTs = userWithTimestamp.copy(id = Some(id), lastLogin = Some(timestamp))
          // verify its the same 
          retrieved.isDefined must beTrue
          retrieved.get must beEqualTo(correctTs)
        }
      }
    }
  }
}

