package com.belleMusica.schemas

import com.belleMusica.entities.User
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Users: Table<User>("users") {
    val id = int("id").primaryKey().bindTo { it.id }
    val username = varchar("username").bindTo { it.username }
    val email = varchar("email").bindTo { it.email }
    val encryptedPassword = varchar("encrypted_password").bindTo { it.encryptedPassword }
    val profilePicture = varchar("image").bindTo { it.profilePicture }
}
