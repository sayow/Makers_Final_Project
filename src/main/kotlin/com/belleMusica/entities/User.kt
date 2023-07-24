package com.belleMusica.entities


import org.ktorm.entity.Entity

interface User : Entity<User> {
    companion object : Entity.Factory<User>()
    var id: Int
    var username: String
    var email: String
    var encryptedPassword: String
    var profilePicture: String
}