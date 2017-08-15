package models

case class UserData(id: Int,
                    firstName: String,
                    middleName: Option[String],
                    lastName: String,
                    userName: String,
                    password: String,
                    mobileNo: Long,
                    gender: String,
                    age: Int,
                    isAdmin: Boolean = false,
                    isEnabled: Boolean = true)

case class AssignmentData(id: Int, title: String,
                          description: String)

case class HobbyData(id: Int,
                     hobby: String)

case class UserHobbyData(userName: String,
                         hobbyName: String)

case class UserProfileData(firstName: String,
                           middleName: Option[String],
                           lastName: String, mobileNo: Long,
                           gender: String, age: Int)

