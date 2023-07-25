package com.belleMusica.handlers

import com.belleMusica.entities.User
import com.belleMusica.schemas.Users
import com.belleMusica.viewmodel.LoginViewModel
import database
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.removeCookie
import org.http4k.lens.LensFailure
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.first
import org.ktorm.entity.sequenceOf
import org.mindrot.jbcrypt.BCrypt
import requiredEmailField
import requiredLoginCredentialsLens
import requiredPasswordField
import sessionCache
import templateRenderer
import java.util.*

fun newSessionHandler(): HttpHandler = {
    val viewModel = LoginViewModel("", "", "")

    Response(Status.OK)
        .body(templateRenderer(viewModel))
}

fun createSessionHandler(): HttpHandler = { request: Request ->
    val form = requiredLoginCredentialsLens.extract(request)
    val email = requiredEmailField(form)
    val password = requiredPasswordField(form)

    val user = authenticateUser(email, password)
    if (user == null) {
        val viewModel = LoginViewModel("", "", "The user could not be authenticated, please try again.")
        Response(Status.BAD_REQUEST).header("Location", "/sessions/new")
        Response(Status.BAD_REQUEST).body(templateRenderer(viewModel))
    } else {
        getSpotifyAlbums(user)
        injectSessionCookie(
            Response(Status.FOUND).header("Location", "/albums"),
            user
        )
    }
}

fun loginFailResponse (failure: LensFailure): Response {
    return Response(Status.BAD_REQUEST).body(templateRenderer(LoginViewModel(
        "",
        "",
        "The user could not be authenticated, please try again.")))
}

fun destroySessionHandler(): HttpHandler = {
    val cookie = it.cookie("belle_musica_session_id")
    val sessionId = cookie?.value
    if (sessionId != null) {
        sessionCache.invalidate(sessionId)
    }


    val expiredCookie = Cookie("belle_musica_session_id", "", path = "/", maxAge = 0)
    Response(Status.SEE_OTHER)
        .header("Location", "/")

        .removeCookie("belle_musica_session_id")
        .cookie(expiredCookie)
        .body("")
}

private fun authenticateUser(email: String, password: String): User? {
    try {
        val user = database.sequenceOf(Users)
            .filter { it.email eq email }
            .first()

        if (BCrypt.checkpw(password, user.encryptedPassword)) {
            return user
        } else {
            return null
        }
    } catch (e: NoSuchElementException) {
        return null
    }
}

private fun injectSessionCookie(response: Response, user: User): Response {
    val sessionId = UUID.randomUUID().toString()
    sessionCache.put(sessionId, user.id)

    return response.cookie(Cookie("belle_musica_session_id", sessionId))
}