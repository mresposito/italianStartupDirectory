package org.startupDirectory.data

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import scala.slick.driver.H2Driver
import scala.slick.session.Session
import scala.slick.session.Database
import play.api.db._
import play.api.Play.current

class EntityStoreSpec extends Specification {

  var store: EntityStore = new EntityStore(H2Driver)
  val baseEntity =  Entity(None, "michele", "m@e.com")
  val entityTwo =  Entity(None, "tonino", "met@e.com")

  "Entity store" should {

    "insert new entity" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          val ret = store.insertEntity(baseEntity)
          ret.toInt must beGreaterThan(0)
        }
      }
    }

    "insert and retrieve new entity" in {
      running(FakeApplication()) {
   
        Database.forDataSource(DB.getDataSource()).withSession { implicit session: Session =>
          store.insertEntity(baseEntity)
          // retrieve the inserted
          val ret = store.byEmail(baseEntity.email)
          ret.isDefined must beTrue

          val retrieved = ret.get
          retrieved.email must beEqualTo(baseEntity.email)
          retrieved.name must beEqualTo(baseEntity.name)
          retrieved.id.isDefined must beTrue
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
