package com.belleMusica.handlers

import com.belleMusica.entities.Album
import com.belleMusica.entities.User
import com.belleMusica.viewmodel.LandingPageViewModel
import org.http4k.core.*
import templateRenderer


fun getLandingPage(contexts: RequestContexts): HttpHandler = {request: Request ->
    val currentUser: User? = contexts[request]["user"]

    getSpotifyAlbums(null)
    val theNewList = mutableListOf<Album>()
    albumList.forEach{
        if(it.numberLikes != 0){
            theNewList.add(it)
        }
    }

    val landingPageViewModel = LandingPageViewModel( theNewList.sortedByDescending { it.numberLikes }.take(10), currentUser)

    Response(Status.OK)
        .body(templateRenderer(landingPageViewModel))
}