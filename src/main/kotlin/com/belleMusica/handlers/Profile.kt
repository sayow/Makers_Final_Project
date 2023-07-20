package com.belleMusica.handlers

import com.belleMusica.entities.*
import com.belleMusica.schemas.Users
import com.belleMusica.viewmodel.ProfileViewModel
import database
import org.http4k.core.*
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import templateRenderer
import java.io.File
import java.util.*

fun viewProfile(contexts: RequestContexts):  HttpHandler = { request: Request ->
    val currentUser: User? = contexts[request]["user"]
    val viewModel = ProfileViewModel(currentUser = currentUser)
    Response(Status.OK)
        .body(templateRenderer(viewModel))
}

fun updateProfilePicture(contexts: RequestContexts): HttpHandler = {request: Request ->
    val receivedForm = MultipartFormBody.from(request)
    val file = receivedForm.file("profile_picture")
    val filename = file?.filename
    val contentType = file?.contentType
    val inputStream = file?.content
    if (filename != null && contentType != null && inputStream != null) {
        val uniqueFilename = UUID.randomUUID().toString()
        val extension = filename.substringAfterLast(".", "")
        val savedFilename = "$uniqueFilename.$extension"
        //TODO: Remember to change/hide the absolute path before merging
        val uploadDirectory = "/Users/ddu4537/Projects/static"
        val savedFile = File(uploadDirectory, savedFilename)
        inputStream.use { input ->
            savedFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val pictureLink = "static-photos/$savedFilename"

        val currentUser: User? = contexts[request]["user"]
        database.update(Users) {
            set(it.profilePicture, pictureLink)
            if (currentUser != null) {
                where {
                    it.id eq currentUser.id
                }
            }
        }
        Response(Status.SEE_OTHER)
            .header("Location", "/profile")
    } else {
        Response(Status.BAD_REQUEST).body("No picture uploaded")
    }
}