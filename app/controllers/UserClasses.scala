package controllers

case class User(name: String,
                middleName: Option[String],
                lastName: String,
                userName: String,
                password: String,
                re_enterPassword: String,
                mobileNo: Long,
                gender: String, age: Int,
                hobbies: List[String])

case class Profile(name: String,
                   middleName: Option[String], lastName: String,
                   mobileNo: Long,
                   gender: String,
                   age: Int,
                   hobbies: List[String])

case class LoginUser(username: String,
                     password: String)

case class Hobbies(reading: Boolean,
                   music: Boolean,
                   movies: Boolean)

case class ForgetPassword(email: String,
                          newPassword: String,
                          confirmPassword: String)

case class Assignment(title:String,
                      description:String)