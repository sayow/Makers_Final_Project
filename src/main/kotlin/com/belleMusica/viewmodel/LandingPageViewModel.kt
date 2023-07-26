package com.belleMusica.viewmodel
import com.belleMusica.entities.Album
import com.belleMusica.entities.User
import org.http4k.template.ViewModel

data class LandingPageViewModel(val mostPopularAlbums: List<Album>,val currentUser: User? ): ViewModel