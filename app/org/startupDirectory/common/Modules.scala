package org.startupDirectory.common
 
import com.tzavellas.sse.guice.ScalaModule
import org.startupDirectory.util.Clock
import org.startupDirectory.data.{DatabaseConnection, PlayDBConnection}
import org.startupDirectory.data.{Profile, H2Profile}
import scala.slick.driver.H2Driver
import scala.slick.driver.ExtendedProfile

trait CommodModule extends ScalaModule {

  def stardantConf = {
    bind[DatabaseConnection].to[PlayDBConnection]
    bind[Profile].to[H2Profile]
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
