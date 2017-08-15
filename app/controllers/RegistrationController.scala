package controllers

import com.google.inject.Inject
import models.{HobbiesRepository, UserData, UserHobbiesRepository, UserRepository}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RegistrationController @Inject()(userRepository: UserRepository,
                                       hobbyRepository: HobbiesRepository,
                                       userHobbiesRepository: UserHobbiesRepository,
                                       forms: UserForms,
                                       val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val hobbiesList: Future[List[String]] =  hobbyRepository.getHobbies

  def showRegisterForm(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

   hobbiesList.map {
      case hobbies: List[String] => Ok(views.html.register(forms.registerForm, hobbies))
      case _ => InternalServerError("Could not retrieve hobbies from database")
    }
  }

  def handleRegister(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

    forms.registerForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("error occurred" + formWithErrors)
        hobbiesList.map(hobbies=>BadRequest(views.html.register(formWithErrors, hobbies)))
      },
      userData => {

        userRepository.checkUserExists(userData.userName) flatMap {
          case true => Future.successful(Redirect(routes.LoginController.showLoginForm())
            .flashing("exists" -> "Username already exists. Please sign in."))
          case false =>
            val user: UserData = UserData(0, userData.name, userData.middleName, userData.lastName, userData.userName,
              userData.password, userData.mobileNo, userData.gender, userData.age)

            userRepository.addUser(user).flatMap {
              case true => userHobbiesRepository.addHobbies(userData.userName, userData.hobbies).map {
                case Some(x) if x > 0 => Redirect(routes.UserProfileController.getProfileDetails()).withSession("email"->userData.userName)
                case _ => InternalServerError("failed to add hobbies")
              }

              case false => Future.successful(InternalServerError("failed to add user details"))
            }
        }
      })
  }
}


