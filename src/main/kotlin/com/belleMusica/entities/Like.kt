package com.belleMusica.entities
import org.ktorm.entity.Entity
interface Like : Entity<Like> {
    companion object : Entity.Factory<Like>()
    val id: Int
    var albumId: String
    var userId: Int
}