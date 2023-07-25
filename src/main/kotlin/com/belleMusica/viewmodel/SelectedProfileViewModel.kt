package com.belleMusica.viewmodel

import com.belleMusica.entities.Album
import com.belleMusica.entities.User
import org.http4k.template.ViewModel

data class SelectedProfileViewModel(
    val currentUser: User?,
    val profileUser: User,
    val isFollowed: Boolean,
    val likedAlbums: List<Album>,
    val followedUsers: List<User>
    ): ViewModel