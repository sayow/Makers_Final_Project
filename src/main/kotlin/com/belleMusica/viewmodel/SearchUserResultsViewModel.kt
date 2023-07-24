package com.belleMusica.viewmodel

import com.belleMusica.entities.Album
import com.belleMusica.entities.User
import org.http4k.template.ViewModel

data class SearchUserResultsViewModel(
    val currentUser: User?,
    val foundUsers: List<User>,
    val message: String?
    ): ViewModel