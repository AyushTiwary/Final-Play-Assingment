package models

import org.scalatestplus.play.PlaySpec

class UserRepositoryTest extends PlaySpec {

  val modelsTest = new ModelsTest[UserRepository]
  val mobileNo = 9560790485L
  val age1 = 21
  val age2 = 24
  private val userData = UserData(0, "ayush", None, "tiwari", "ayush.tiwari@gmail.com", "knoldus2017"
    , mobileNo, "male", age1)

  private val userProfileData: UserProfileData = UserProfileData("ayush", None, "tiwari",
    mobileNo, "male", age2)

  "UserRepository" should {

    "add user" in {
      val result = modelsTest.result(modelsTest.repository.addUser(userData))
      result mustEqual true
    }
    "check if username exists" in {
      val result = modelsTest.result(modelsTest.repository.checkUserExists(userData.userName))
      result mustEqual true
    }
    "match user login details" in {
      val result = modelsTest.result(modelsTest.repository.matchUserLoginDetails(userData.userName
        , userData.password))
      result mustEqual true
    }
    "not match invalid user login details" in {
      val result = modelsTest.result(modelsTest.repository.matchUserLoginDetails(userData.userName + "dafsd"
        , userData.password))
      result mustEqual false
    }
    "check if user is enabled" in {
      val result = modelsTest.result(modelsTest.repository.isUserEnabled(userData.userName))
      result mustEqual true
    }
    "update password of existing user" in {
      val result = modelsTest.result(modelsTest.repository.updatePassword(userData.userName
        , userData.password))
      result mustEqual true
    }
    "not update password of invalid user" in {
      val result = modelsTest.result(modelsTest.repository.updatePassword(userData.userName + "s"
        , userData.password))
      result mustEqual false
    }
    "update user details" in {
      val result = modelsTest.result(modelsTest.repository.updateUserDetails(userData.userName,
        userProfileData))
      result mustEqual true
    }
    "get user details" in {
      val result = modelsTest.result(modelsTest.repository.getUserDetails(userData.userName))
      result mustEqual userProfileData
    }



    "enable or disable user" in {
      val result = modelsTest.result(modelsTest.repository.enableOrDisableUser(userData.userName,
        !userData.isEnabled))
      result mustEqual true
    }
    "check if admin" in {
      val result = modelsTest.result(modelsTest.repository.isAdmin(userData.userName))
      result mustEqual false
    }
  }
}
