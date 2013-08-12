package org.startupDirectory.data

import play.api.db._
import play.api.Play.current
import scala.slick.session.Database

trait DatabaseConnection {
  val database: Database
}

class PlayDBConnection extends DatabaseConnection {
  val database: Database = new Database {
    override def createConnection() = DB.getConnection()
  }
}
