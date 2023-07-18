package com.belleMusica.schemas
import com.belleMusica.entities.Like
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text

object Likes: Table<Like>("likes") {
    val id = int("id").primaryKey().bindTo { it.id }
    val albumId = text("album_id").bindTo { it.albumId }
    val userId = int("user_id").bindTo { it.userId }
}
