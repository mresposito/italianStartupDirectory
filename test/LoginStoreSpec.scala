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

class LoginStoreSpec extends MockDAL {
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
          val retrieved = store.getLoginByEmail(baseLogin.email)
          retrieved.isDefined must beTrue
          retrieved.get must beEqualTo(newOpt)
        }
      }
    }

    "create with the right time" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>

          when(clock.now).thenReturn(timestamp)
          val id = store.login(baseLogin)
          id.toInt must beGreaterThan(0)
 
          val newOpt = baseLogin.copy(id = Some(id), created = Some(timestamp), lastLogin = Some(timestamp))
          val retrieved = store.getLoginByEmail(baseLogin.email)
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
          val loginWithTimestamp = baseLogin.copy(created = shortTimestamp)
          val insertId = store.insert(loginWithTimestamp)
          // make new login
          when(clock.now).thenReturn(timestamp)
          val id = store.login(baseLogin)
          id.toInt must beGreaterThan(0)
          // retrieve login 
          val retrieved = store.getLoginByEmail(baseLogin.email)
          val correctTs = loginWithTimestamp.copy(id = Some(id), lastLogin = Some(timestamp))
          // verify its the same 
          retrieved.isDefined must beTrue
          retrieved.get must beEqualTo(correctTs)
        }
      }
    }
  }
}

