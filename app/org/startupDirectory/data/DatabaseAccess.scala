package org.startupDirectory.data

import play.api.db.{DB, DBApi}
import play.api.Play.current
import scala.slick.session.Database

trait DatabaseConnection {
  val database: Database
}

class PlayH2DBConnection extends DatabaseConnection {
  val database: Database = new Database {
    override def createConnection() = DB.getConnection()
  }
}

class TestH2DBConnection extends DatabaseConnection {
  Class.forName("org.h2.Driver")
  val database: Database = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")
}
