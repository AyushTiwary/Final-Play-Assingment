package controllers

import akka.stream.Materializer
import akka.util.Timeout
import models.UserRepository
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.MessagesApi
import play.api.test.FakeRequest
import play.api.test.Helpers.{redirectLocation, status}
import scala.concurrent.duration.DurationInt
import org.mockito.Mockito._
import play.api.mvc.Result

import scala.concurrent.Future

class LoginControllerTest extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  val userRepository: UserRepository = mock[UserRepository]
  val forms: UserForms = mock[UserForms]
  val messages: MessagesApi = mock[MessagesApi]
  when(forms.loginForm).thenReturn(new UserForms().loginForm)
  when(forms.forgetPasswordForm).thenReturn(new UserForms().forgetPasswordForm)
  val loginController = new LoginController(userRepository, forms, messages)

  "LoginController" should {
    "show login form" in {
      val result = loginController.showLoginForm().apply(FakeRequest("GET", "/"))
      implicit val timeout = Timeout(10 seconds)
      status(result) mustEqual 200
    }

    "show forget form" in {
      val result = loginController.showForgetForm().apply(FakeRequest("GET", "/"))
      implicit val timeout = Timeout(10 seconds)
      status(result) mustEqual 200
    }

    "be able to login valid user" in {
      when(userRepository.matchUserLoginDetails("ayush.tiwari@knoldus.in", "knoldus2017"))
        .thenReturn(Future.successful(true))
      when(userRepository.isUserEnabled("ayush.tiwari@knoldus.in")).thenReturn(Future.successful(true))

      val result: Future[Result] = loginController.handleLogin().apply(FakeRequest("GET", "/handlelogin").withFormUrlEncodedBody(
        "username" -> "ayush.tiwari@knoldus.in", "password" -> "knoldus2017"))
      implicit val timeout = Timeout(10 seconds)
      redirectLocation(result) mustBe Some("/getDetails")
    }
    "handle when login details are invalid" in {
      when(userRepository.matchUserLoginDetails("ayush.tiwari@knoldus.in", "knoldus2017"))
        .thenReturn(Future.successful(false))
      when(userRepository.isUserEnabled("ayush.tiwari@knoldus.in")).thenReturn(Future.successful(false))

      val result: Future[Result] = loginController.handleLogin().apply(FakeRequest("GET", "/handlelogin").withFormUrlEncodedBody(
        "username" -> "ayush.tiwari@knoldus.in", "password" -> "knoldus2017"))
      implicit val timeout = Timeout(10 seconds)
      redirectLocation(result) mustBe Some("/login")
    }
    "handle when user is disabled" in {
      when(userRepository.matchUserLoginDetails("ayush.tiwari@knoldus.in", "knoldus2017"))
        .thenReturn(Future.successful(true))
      when(userRepository.isUserEnabled("ayush.tiwari@knoldus.in")).thenReturn(Future.successful(false))

      val result: Future[Result] = loginController.handleLogin().apply(FakeRequest("GET", "/handlelogin").withFormUrlEncodedBody(
        "username" -> "ayush.tiwari@knoldus.in", "password" -> "knoldus2017"))
      implicit val timeout = Timeout(10 seconds)
      redirectLocation(result) mustBe Some("/login")
    }

    "handle the case when passwords are different in forgetPassword form" in {
      val result: Future[Result] = loginController.forgetPassword().apply(FakeRequest("GET", "/handlelogin").withFormUrlEncodedBody(
        "email" -> "ayush.tiwari@knoldus.in", "newPassword" -> "knolder2017", "confirmPassword" -> "knoldus2017"))
      implicit val timeout = Timeout(10 seconds)

      status(result) mustBe 400
    }
    "handle forgetPassword form with success" in {
      when(userRepository.updatePassword("ayush.tiwari@knoldus.in", "knoldus2017")).thenReturn(Future.successful(true))
      val result: Future[Result] = loginController.forgetPassword().apply(FakeRequest("GET", "/handlelogin").withFormUrlEncodedBody(
        "email" -> "ayush.tiwari@knoldus.in", "newPassword" -> "knoldus2017", "confirmPassword" -> "knoldus2017"))
      implicit val timeout = Timeout(10 seconds)

      redirectLocation(result) mustBe Some("/login")
    }
    "handle forgetPassword form when email not exists" in {
      when(userRepository.updatePassword("ayush.tiwari@knoldus.in", "knoldus2017")).thenReturn(Future.successful(false))
      val result: Future[Result] = loginController.forgetPassword().apply(FakeRequest("GET", "/handlelogin").withFormUrlEncodedBody(
        "email" -> "ayush.tiwari@knoldus.in", "newPassword" -> "knoldus2017", "confirmPassword" -> "knoldus2017"))
      implicit val timeout = Timeout(10 seconds)

      redirectLocation(result) mustBe Some("/forget")
    }


  }
}