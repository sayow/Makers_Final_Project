package com.belleMusica.handlers

import com.belleMusica.*
import com.belleMusica.entities.User
import com.belleMusica.schemas.Users
import com.belleMusica.viewmodel.SignUpViewModel
import database
import org.http4k.core.*
import org.http4k.lens.LensFailure
import org.http4k.template.HandlebarsTemplates
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import org.ktorm.entity.*
import org.mindrot.jbcrypt.BCrypt
import requiredEmailField
import requiredPasswordField
import requiredSignupFormLens
import requiredUsernameField
import templateRenderer
import java.io.File
import java.util.*


fun newUserHandler(): HttpHandler = {
    val renderer = HandlebarsTemplates().HotReload("src/main/resources")
    val viewModel = SignUpViewModel("", "", "","")

    Response(Status.OK).body(renderer(viewModel))
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    return emailRegex.toRegex().matches(email)
}

fun isValidPassword(password: String): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    return passwordRegex.toRegex().matches(password)
}

fun signupFailResponse(failure: LensFailure): Response {
    return Response(Status.BAD_REQUEST).body(
        templateRenderer(
            SignUpViewModel(
                "",
                "",
                "",
                "The user could not sign up, please try again."
            )
        )
    )
}

fun createUserHandler(): HttpHandler = { request: Request ->
    val form = requiredSignupFormLens(request)
    val inputEmail = requiredEmailField(form)
    val inputPassword = requiredPasswordField(form)
    val inputUsername = requiredUsernameField(form)

    val userExists = database.sequenceOf(Users).filter { it.email eq inputEmail }.any()

    if (userExists) {
        Response(Status.BAD_REQUEST).body(
            templateRenderer(
                SignUpViewModel(
                    "",
                    "",
                    "",
                    "The user already exists, choose another email."
                )
            )
        )
    } else if (!isValidEmail(inputEmail)) {
        Response(Status.BAD_REQUEST).body(
            templateRenderer(
                SignUpViewModel(
                    "",
                    "",
                    "",
                    "The email is incorrect, choose a valid email."
                )
            )
        )
    } else if (!isValidPassword(inputPassword)) {
        Response(Status.BAD_REQUEST).body(
            templateRenderer(
                SignUpViewModel(
                    "",
                    "",
                    "",
                    "The password should contain at least one digit, one lowercase letter, one uppercase letter, one special character and have a minimum length of 8 characters."
                )
            )
        )
    } else {

        val newUser = User {
            email = inputEmail
            encryptedPassword = BCrypt.hashpw(inputPassword, BCrypt.gensalt())
            username = inputUsername
        }

        database.sequenceOf(Users).add(newUser)

        Response(Status.FOUND).header("Location", "/sessions/new")
    }
}

//fun getUpdateUserHandler(contexts: RequestContexts): HttpHandler = { request: Request ->
//    val renderer = HandlebarsTemplates().HotReload("src/main/resources")
//
//    val currentUser: User? = contexts[request]["user"]
//    val viewModel = UpdateUserViewModel(currentUser)
//
//    Response(Status.OK).body(renderer(viewModel))
//}
//
//fun updateUserHandler(contexts: RequestContexts): HttpHandler = { request: Request ->
//    val receivedForm = MultipartFormBody.from(request)
//    val file = receivedForm.file("profile_picture")
//    val filename = file?.filename
//    val contentType = file?.contentType
//    val inputStream = file?.content
//    if (filename != null && contentType != null && inputStream != null) {
//        val uniqueFilename = UUID.randomUUID().toString()
//        val extension = filename.substringAfterLast(".", "")
//        val savedFilename = "$uniqueFilename.$extension"
//        // TODO: Remember to change/hide the absolute path before merging
//        val uploadDirectory = "/**/**/**/acebook-sapphire/src/main/resources/static"
//        val savedFile = File(uploadDirectory, savedFilename)
//        inputStream.use { input ->
//            savedFile.outputStream().use { output ->
//                input.copyTo(output)
//            }
//        }
//        val pictureLink = "/static/$savedFilename"
//        val currentUser: User? = contexts[request]["user"]
//        database.update(Users) {
//            set(it.profilePicture, pictureLink)
//            if (currentUser != null) {
//                where {
//                    it.id eq currentUser.id
//                }
//            }
//        }
//        Response(Status.SEE_OTHER)
//            .header("Location", "/")
//    } else {
//        Response(Status.BAD_REQUEST).body("No picture uploaded")
//    }
//}