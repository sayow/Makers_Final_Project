package com.belleMusica.entities

data class Album(
    val id: String,
    var artistName: String,
    var albumName: String,
    var imageUrl: String,
    var numberLikes: Int
)