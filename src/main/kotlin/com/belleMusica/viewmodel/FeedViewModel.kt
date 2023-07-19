package com.belleMusica.viewmodel
import com.belleMusica.entities.*
import org.http4k.template.ViewModel

data class FeedViewModel(val albumList: List<Album>, val currentUser: User?): ViewModel