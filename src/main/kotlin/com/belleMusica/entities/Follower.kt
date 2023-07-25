package com.belleMusica.entities
import org.ktorm.entity.Entity
interface Follower : Entity<Follower> {
    companion object : Entity.Factory<Follower>()
    val id: Int
    var followerId: Int
    var followedUserId: Int
}