import play.api.db.DB
import play.api.GlobalSettings
import scala.slick.driver.H2Driver
import scala.slick.session.Database
import scala.slick.session.Session
import play.api.Application
import play.api.Play.current
import org.startupDirectory.data.DAL
import org.startupDirectory.util.Clock
 
 
object Global extends GlobalSettings {
 
  override def onStart(app: Application) {

    lazy val database = Database.forDataSource(DB.getDataSource())
    val dal = new DAL(H2Driver, new Clock)
    database.withSession { implicit session: Session =>
      try {
        dal.create
      } catch {
        case _: org.h2.jdbc.JdbcSQLException => println("table already exists")
      }
    }
  }
}
