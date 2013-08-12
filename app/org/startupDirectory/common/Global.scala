package org.startupDirectory.common

import com.google.inject.Guice
import org.startupDirectory.data.DAL
import org.startupDirectory.util.Clock
import scala.slick.driver.H2Driver
import scala.slick.session.Database
import scala.slick.session.Session
import play.api.Application
import play.api.db.DB
import play.api.GlobalSettings
import play.api.Play
import play.api.Play.current
import org.startupDirectory.data.{Profile, H2Profile}
 
object Global extends GlobalSettings {
 
  override def onStart(app: Application) {

    lazy val database = Database.forDataSource(DB.getDataSource())
    val dal = new DAL(new H2Profile, new Clock)
    database.withSession { implicit session: Session =>
      try {
        dal.create
      } catch {
        case _: org.h2.jdbc.JdbcSQLException => println("table already exists")
      }
    }
  }

  private lazy val injector = {
    Play.isProd match {
      case true => Guice.createInjector(new ProdModule)
      case false => Guice.createInjector(new DevModule)
    }
  }

  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }
}
