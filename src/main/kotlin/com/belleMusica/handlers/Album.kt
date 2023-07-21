package com.belleMusica.handlers

import com.belleMusica.entities.*
import com.belleMusica.schemas.Likes
import com.belleMusica.viewmodel.FeedViewModel
import database
import templateRenderer
import okhttp3.OkHttpClient
import org.http4k.core.*
import okhttp3.Request as APIRequest
import org.json.JSONObject
import org.ktorm.dsl.and
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

fun getSpotifyAlbums() {
    val client = OkHttpClient()
    val searchQuery = ""
    val request = APIRequest.Builder()
        .url("https://spotify23.p.rapidapi.com/search/?q=%{$searchQuery}%3E&type=multi&offset=0&limit=100&numberOfTopResults=20")
        .get()
        .addHeader("X-RapidAPI-Key", "85abc57478msh62edece0b781437p1cefecjsnf08b903629a0")
        .addHeader("X-RapidAPI-Host", "spotify23.p.rapidapi.com")
        .build()
    val response = client.newCall(request).execute()
    val responseContent = response.body?.string()
    val jsonObject = JSONObject(responseContent)
    val itemsArray = jsonObject.getJSONObject("albums").getJSONArray("items")
    albumList.clear()
    for (i in 0 until itemsArray.length()) {
        val dataObject = itemsArray.getJSONObject(i).getJSONObject("data")
        albumList.add(createAlbum(dataObject))

    }
}

fun createAlbum(dataObject: JSONObject): Album {
    // Parse the responseContent JSON and extract the id, album, artist, and image data
    val albumId = dataObject.getString("uri").split(":").last()
    val artistsArray = dataObject.getJSONObject("artists").getJSONArray("items")
    val artistObject = artistsArray.getJSONObject(0)
    val artistName = artistObject.getJSONObject("profile").getString("name")
    val albumName = dataObject.getString("name")
    val coverArtArray = dataObject.getJSONObject("coverArt").getJSONArray("sources")
    val imageUrl = coverArtArray.getJSONObject(0).getString("url")
    return Album(albumId, artistName, albumName, imageUrl, getNumberLikesAlbum(albumId))
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
                }
            }
            database.sequenceOf(Likes).add(newLike)
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
