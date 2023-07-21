package com.belleMusica.handlers

import com.belleMusica.entities.Album
import com.belleMusica.viewmodel.LandingPageViewModel
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import templateRenderer


fun getLandingPage(): HttpHandler = {
    getSpotifyAlbums()
    val theNewList = mutableListOf<Album>()
    albumList.forEach{
        if(it.numberLikes != 0){
            theNewList.add(it)
        }
    }
    val landingPageViewModel = LandingPageViewModel( theNewList.sortedByDescending { it.numberLikes },"Welcome to Belle Musica!",)

    Response(Status.OK)
        .body(templateRenderer(landingPageViewModel))
}