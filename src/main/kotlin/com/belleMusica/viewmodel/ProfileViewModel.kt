package com.belleMusica.viewmodel

import com.belleMusica.entities.Album
import com.belleMusica.entities.User
import org.http4k.template.ViewModel

data class ProfileViewModel(
    val currentUser: User?,
    val likedAlbums: List<Album>,
    val followedUsers: List<User>
    ): ViewModel