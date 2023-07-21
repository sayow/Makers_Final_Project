package com.belleMusica.handlers

import com.belleMusica.viewmodel.LandingPageViewModel
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import templateRenderer


fun getLandingPage(): HttpHandler = {
    val landingPageViewModel = LandingPageViewModel("Welcome to Belle Musica!")
    Response(Status.OK)
        .body(templateRenderer(landingPageViewModel))
}