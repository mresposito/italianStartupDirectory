package org.startupDirectory.common
 
import com.tzavellas.sse.guice.ScalaModule
import org.startupDirectory.util.Clock
import org.startupDirectory.data.{DatabaseConnection, PlayH2DBConnection}
import org.startupDirectory.data.{Profile, H2Profile}
import scala.slick.driver.H2Driver
import scala.slick.driver.ExtendedProfile

trait CommodModule extends ScalaModule {

  def stardantConf = {
    bind[DatabaseConnection].to[PlayH2DBConnection]
    bind[Profile].to[H2Profile]
  
  // val entityStore: DAL = new DAL(H2Driver, new Clock);
  // val database: Database = new Database {
  //   override def createConnection() = DB.getConnection()
  // }
  }
}
 
class ProdModule extends CommodModule {
  def configure = {
    stardantConf
  }
}

class DevModule extends CommodModule {
  def configure = {
    stardantConf
  }
}
