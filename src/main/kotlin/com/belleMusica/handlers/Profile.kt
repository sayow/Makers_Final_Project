package com.belleMusica.handlers

import com.belleMusica.entities.*
import com.belleMusica.viewmodel.ProfileViewModel
import org.http4k.core.*
import templateRenderer

fun viewProfile(contexts: RequestContexts):  HttpHandler = { request: Request ->
    val currentUser: User? = contexts[request]["user"]
    val viewModel = ProfileViewModel(currentUser = currentUser)
    Response(Status.OK)
        .body(templateRenderer(viewModel))
}