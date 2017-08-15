package models

import org.scalatestplus.play.PlaySpec


class UserHobbiesRepositoryTest extends PlaySpec {

  val modelsTest = new ModelsTest[UserHobbiesRepository]
  val hobbies = List("chess", "coding", "testing")
  val userName = "ayush.tiwari@knoldus.in"

  "UserHobbiesRepository" should {

    "add hobbies" in {
      val result = modelsTest.result(modelsTest.repository.addHobbies(userName, hobbies))
      result.map(value => assert(value > 0))
    }
    "delete hobbies" in {
      val result = modelsTest.result(modelsTest.repository.deleteHobbies(userName))
      result mustEqual true
    }
    "update hobbies" in {
      modelsTest.result(modelsTest.repository.addHobbies(userName, hobbies))
      val updateresult = modelsTest.result(modelsTest.repository.updateHobbies(userName, hobbies.take(2)))
      updateresult.map(value => assert(value > 0))
    }
  }
}
