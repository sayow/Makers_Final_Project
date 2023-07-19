package com.belleMusica.handlers

import com.belleMusica.entities.*
import com.belleMusica.viewmodel.FeedViewModel
import templateRenderer
import okhttp3.OkHttpClient
import org.http4k.core.*
import okhttp3.Request as APIRequest
import org.json.JSONObject

val albumList = mutableListOf<Album>()

fun getAlbumPage(request: Request, contexts: RequestContexts): Response {
    val client = OkHttpClient()
    val currentUser: User? = contexts[request]["user"]

    println("currentUser: $currentUser")
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
    for (i in 0 until itemsArray.length()) {
        val dataObject = itemsArray.getJSONObject(i).getJSONObject("data")
        albumList.add(createAlbum(dataObject))
    }
    val feeds = FeedViewModel(albumList, currentUser = currentUser)
    val theContent = templateRenderer(feeds)
    return Response(Status.OK).body(theContent)
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
    return Album(albumId, artistName, albumName, imageUrl)
}

fun usernameView(contexts: RequestContexts):  HttpHandler = { request: Request ->
    val currentUser: User? = contexts[request]["user"]
    val viewModel = FeedViewModel(albumList, currentUser = currentUser)
    Response(Status.OK)
        .body(templateRenderer(viewModel))
}