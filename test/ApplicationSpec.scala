package test

import org.specs2.mutable._
import org.startupDirectory.data.StoreFormatters
import org.startupDirectory.data.Login
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc.Session
import play.api.libs.json.Json

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  import StoreFormatters._

  def FakeLoggedRequest(method: String, path: String) = FakeRequest(method, path).withSession(
      ("user.id" , "0"),
      ("user.email" , "michele@gmail.com"),
      ("user.loginSecret" , "833824640"))
  
  "Application" should {
    
    "Page Render tests" in {

      "render the index page" in {
        running(FakeApplication()) {
          val home = route(FakeRequest(GET, "/")).get
          
          status(home) must equalTo(OK)
          contentType(home) must beSome.which(_ == "text/html")
          contentAsString(home) must contain ("Italian Startup Directory")
          contentAsString(home) must contain ("Login")
        }
      }

      "render the login welcome page" in {
        running(FakeApplication()) {
          val home = route(FakeLoggedRequest(GET, "/welcome")).get
          
          status(home) must equalTo(OK)
          contentType(home) must beSome.which(_ == "text/html")
          contentAsString(home) must contain ("Italian Startup Directory")
          contentAsString(home) must contain ("Logout")
        }
      }

      "render the logged in index page" in {
        running(FakeApplication()) {
          val home = route(FakeLoggedRequest(GET, "/")).get
          
          status(home) must equalTo(OK)
          contentType(home) must beSome.which(_ == "text/html")
          contentAsString(home) must contain ("Logout")
          contentAsString(home) must contain ("Welcome!")
        }
      }
    }

    "Login tests" in {

      "user should be able to login" in {
        running(FakeApplication()) {
          val baseLogin = Login("michele", "m@e.com", "facebook", "881133")
          val request = FakeRequest(POST, "/fbLogin").withJsonBody(Json.toJson(baseLogin))
          val home = route(request).get
          val userFromRequest = Json.parse(contentAsString(home)).as[Login]
          
          status(home) must equalTo(200) 
          val ssn = session(home)
          ssn.get("user.id").toString must beEqualTo(userFromRequest.id.toString)
          ssn.get("user.email").get must beEqualTo(baseLogin.email)
          ssn.get("user.loginSecret").get must beEqualTo(baseLogin.loginSecret)
        }
      }

      "user with no credentials should not login" in {
        running(FakeApplication()) {
          val home = route(FakeRequest(POST, "/fbLogin")).get
          
          status(home) must equalTo(400) // TODO: change to 404
          // contentType(home) must beSome.which(_ == "text/html")
          // contentAsString(home) must contain ("Logout")
        }
      }

      def checkLogOut(session: Session) = {
        session.get("user.id") must beEqualTo(None)
        session.get("user.email") must beEqualTo(None)
        session.get("user.loginSecret") must beEqualTo(None)
      }

      "if logged out, should stay logged out" in {
        running(FakeApplication()) {
          val home = route(FakeRequest(POST, "/logout")).get
          
          status(home) must equalTo(OK)
          checkLogOut(session(home))
        }
      }
      
      "if logged in, should be logged out" in {
        running(FakeApplication()) {
          val home = route(FakeLoggedRequest(POST, "/logout")).get
          
          status(home) must equalTo(OK)
          checkLogOut(session(home))
        }
      }
    } /* end login tests */
  }
}
