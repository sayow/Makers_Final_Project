package com.belleMusica.schemas
import com.belleMusica.entities.Follower
import org.ktorm.schema.Table
import org.ktorm.schema.int
object Followers: Table<Follower>("followers") {
    val id = int("id").primaryKey().bindTo { it.id }
    val followerId = int("follower_id").bindTo { it.followerId }
    val followedUserId = int("followed_user_id").bindTo { it.followedUserId }
}
