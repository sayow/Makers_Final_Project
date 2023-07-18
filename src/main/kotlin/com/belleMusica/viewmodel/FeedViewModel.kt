package com.belleMusica.viewmodel
import com.belleMusica.entities.Album
import org.http4k.template.ViewModel

data class FeedViewModel(val albumList: List<Album>): ViewModel