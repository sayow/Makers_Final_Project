package com.belle_musica

import com.belleMusica.schemas.Likes
import com.belleMusica.schemas.Users
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import database
import okhttp3.OkHttpClient
import org.http4k.client.DualSyncAsyncHttpHandler
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.http4k.lens.WebForm
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ktorm.dsl.deleteAll
import requiredLoginCredentialsLens
import requiredSignupFormLens

class AlbumsPageTest {

    @BeforeEach
    fun setup() {
        database.deleteAll(Users)
        database.deleteAll(Likes)
    }

    @Test
    fun `Get the home page`() {
        val client = OkHttp()

        val response: Response = client(
            Request(Method.GET, "http://localhost:9999/albums")
        )

        assertThat(response, hasStatus(Status.OK))
        assertThat(
            response,
            hasBody(containsSubstring("Albums"))
        )
    }

//    @Test
//    fun `Like an album and see order change`() {
//        val client = OkHttp()
//        signupAndLogin(client)
//        val likedAlbumId = "4DrZfbV5FB2Hwzpq6rwArp"
//        val response: Response = client(
//            Request(Method.GET, "http://localhost:9999/like/$likedAlbumId")
//        )
//
//        assertThat(response, hasStatus(Status.OK))
//        assertThat(
//            response,
//            hasBody(containsSubstring("Albums"))
//        )
//    }

    private fun signupAndLogin(client: DualSyncAsyncHttpHandler) {
        client(
            Request(Method.POST, "http://localhost:9999/users").with(
                requiredSignupFormLens of WebForm(mapOf(
                    "email" to listOf("test@acebook.com"),
                    "password" to listOf("Password1@”"),
                    "username" to listOf("test_username")
                ))
            )
        )

        client(
            Request(Method.POST, "http://localhost:9999/sessions").with(
                requiredLoginCredentialsLens of WebForm(mapOf(
                    "email" to listOf("test@acebook.com"),
                    "password" to listOf("Password1@”")
                ))
            )
        )
    }
}