package controllers

import models.{HobbiesRepository, UserData, UserHobbiesRepository, UserRepository}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.MessagesApi
import play.api.test.FakeRequest
import akka.stream.Materializer
import play.api.mvc.Result

import scala.concurrent.Future
import play.api.test.Helpers._

class RegistrationControllerTest extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  val userRepository: UserRepository = mock[UserRepository]
  val hobbiesRepository: HobbiesRepository = mock[HobbiesRepository]
  val userHobbiesRepository: UserHobbiesRepository = mock[UserHobbiesRepository]
  val forms: UserForms = mock[UserForms]
  val messages: MessagesApi = mock[MessagesApi]

  when(forms.registerForm).thenReturn(new UserForms().registerForm)
  when(hobbiesRepository.getHobbies).thenReturn(Future.successful(List("chess", "Table Tennis", "Coding")))

  val registrationController: RegistrationController = new RegistrationController(userRepository, hobbiesRepository
    , userHobbiesRepository, forms, messages)

  "RegistrationController" should {
    "show registration form" in {
      val result = registrationController.showRegisterForm().apply(FakeRequest("GET", "/"))
      status(result) mustEqual OK

    }
    "be able to save signup details from registration form" in {
      when(userRepository.checkUserExists("ayush.tiwari@knoldus.in")).thenReturn(Future.successful(false))
      val mobileNo = 9560790485L
      val age = 20
      val user: UserData = UserData(0, "ayush", None, "tiwari", "ayush.tiwari@knoldus.in",
        "knoldus2017", mobileNo, "male", age)
      when(userRepository.addUser(user)).thenReturn(Future.successful(true))
      when(userHobbiesRepository.addHobbies(user.userName, List("chess"))).thenReturn(Future.successful(Some(age)))

      val result: Future[Result] = registrationController.handleRegister().apply(FakeRequest("GET", "/handleregister").withFormUrlEncodedBody(
        "name" -> "ayush", "middleName" -> "", "lastName" -> "tiwari", "userName" -> "ayush.tiwari@knoldus.in", "password" -> "knoldus2017",
        "re_enterPassword" -> "knoldus2017", "mobileNo" -> "9560790485", "gender" -> "male", "age" -> "20",
        "hobbies[0]" -> "chess"))

      redirectLocation(result) mustBe Some("/getDetails")
    }


    "handle the case where username already exists" in {
      when(userRepository.checkUserExists("ayush.tiwari@knoldus.in")).thenReturn(Future.successful(true))
      val result: Future[Result] = registrationController.handleRegister().apply(FakeRequest("GET", "/handleregister").withFormUrlEncodedBody(
        "name" -> "ayush", "middleName" -> "", "lastName" -> "tiwari", "userName" -> "ayush.tiwari@knoldus.in", "password" -> "knoldus2017",
        "re_enterPassword" -> "knoldus2017", "mobileNo" -> "9560790485", "gender" -> "male", "age" -> "20",
        "hobbies[0]" -> "chess"))

      redirectLocation(result) mustBe Some("/login")
    }

    "handle bad request in registration form" in {
      val result: Future[Result] = registrationController.handleRegister().apply(FakeRequest("GET", "/handleregister").withFormUrlEncodedBody(
        "name" -> "ayush", "middleName" -> "", "lastName" -> "tiwari", "userName" -> "ayush.tiwari@knoldus.in", "password" -> "knoldus2017",
        "re_enterPassword" -> "knoldus20", "mobileNo" -> "9560790485", "gender" -> "male", "age" -> "20",
        "hobbies[0]" -> "reading"))

      status(result) mustBe 400
    }
  }

}
