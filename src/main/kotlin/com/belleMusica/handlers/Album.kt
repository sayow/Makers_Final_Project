package com.belleMusica.handlers

import com.belleMusica.entities.*
import com.belleMusica.schemas.Followers
import com.belleMusica.schemas.Likes
import com.belleMusica.viewmodel.FeedViewModel
import database
import templateRenderer
import okhttp3.OkHttpClient
import org.http4k.core.*
import okhttp3.Request as APIRequest
import org.json.JSONObject
import org.ktorm.dsl.and
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

val albumList = mutableListOf<Album>()

fun getAlbumPage(contexts: RequestContexts): HttpHandler = { request ->
    val currentUser: User? = contexts[request]["user"]
    val orderedAlbumList = albumList.sortedByDescending { it.numberLikes }
    val feeds = FeedViewModel(orderedAlbumList, currentUser)
    val theContent = templateRenderer(feeds)
    Response(Status.OK).body(theContent)
}

fun getSpotifyAlbums(currentUser: User?) {
    val client = OkHttpClient()
    val searchQuery = ""
    val request = APIRequest.Builder()
        .url("https://spotify23.p.rapidapi.com/search/?q=%{$searchQuery}%3E&type=multi&offset=0&limit=100&numberOfTopResults=20")
        .get()
        .addHeader("X-RapidAPI-Key",  dotenv["API_KEY"])
        .addHeader("X-RapidAPI-Host", "spotify23.p.rapidapi.com")
        .build()
    val response = client.newCall(request).execute()
    val responseContent = response.body?.string()
    val jsonObject = JSONObject(responseContent)
    val itemsArray = jsonObject.getJSONObject("albums").getJSONArray("items")
    albumList.clear()
    for (i in 0 until itemsArray.length()) {
        val dataObject = itemsArray.getJSONObject(i).getJSONObject("data")
        albumList.add(createAlbum(dataObject, currentUser))

    }
}

fun createAlbum(dataObject: JSONObject, currentUser: User?): Album {
    // Parse the responseContent JSON and extract the id, album, artist, and image data
    val albumId = dataObject.getString("uri").split(":").last()
    val artistsArray = dataObject.getJSONObject("artists").getJSONArray("items")
    val artistObject = artistsArray.getJSONObject(0)
    val artistName = artistObject.getJSONObject("profile").getString("name")
    val albumName = dataObject.getString("name")
    val coverArtArray = dataObject.getJSONObject("coverArt").getJSONArray("sources")
    val imageUrl = coverArtArray.getJSONObject(0).getString("url")
    val isAlbumLikedByCurrentUser =
        if(currentUser!=null) isAlbumLikedByUser(albumId, currentUser.id)
        else null
    return Album(albumId, artistName, albumName, imageUrl,
                getNumberLikesAlbum(albumId),
                isAlbumLikedByCurrentUser)
}

fun usernameView(contexts: RequestContexts):  HttpHandler = { request: Request ->
    val currentUser: User? = contexts[request]["user"]
    val viewModel = FeedViewModel(albumList, currentUser = currentUser)
    Response(Status.OK)
        .body(templateRenderer(viewModel))
}

fun likeAlbum(contexts: RequestContexts, request: Request, likedAlbumId: String): Response {
    val currentUser: User? = contexts[request]["user"]
    if (currentUser != null) {
        if (!isAlbumLikedByUser(likedAlbumId, currentUser.id)) {
            val newLike = Like {
                userId = currentUser.id
                albumId = likedAlbumId
            }
            albumList.forEach() {
                if (it.id == likedAlbumId) {
                    it.numberLikes += 1
                    it.isLikedByCurrentUser = true
                }
            }
            database.sequenceOf(Likes).add(newLike)
        } else {
            database.delete(Likes) { like ->
                (currentUser.id eq like.userId) and (likedAlbumId eq like.albumId)
            }
            albumList.forEach() {
                if (it.id == likedAlbumId) {
                    it.numberLikes -= 1
                    it.isLikedByCurrentUser = false
                }
            }
        }
    }
    return Response(Status.SEE_OTHER)
        .header("Location", "/albums")
        .body("")
}

fun getNumberLikesAlbum(albumId: String): Int {
    return  database.sequenceOf(Likes)
        .filter{it.albumId eq albumId}
        .toList().count()
}
fun isAlbumLikedByUser(albumId: String, userId: Int): Boolean {
    return database.sequenceOf(Likes)
        .filter{(it.albumId eq albumId) and (it.userId eq userId)}
        .toList()
        .isNotEmpty()
}
