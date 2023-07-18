package com.belleMusica.handlers

import com.belleMusica.entities.Album
import com.belleMusica.viewmodel.FeedViewModel
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import templateRenderer
import okhttp3.OkHttpClient
import okhttp3.Request as APIRequest
import org.json.JSONObject

val albumList = mutableListOf<Album>()

fun getAlbumPage(request: Request): Response {
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
    for (i in 0 until itemsArray.length()) {
        val dataObject = itemsArray.getJSONObject(i).getJSONObject("data")
        albumList.add(createAlbum(dataObject))
    }
    val feeds = FeedViewModel(albumList)
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