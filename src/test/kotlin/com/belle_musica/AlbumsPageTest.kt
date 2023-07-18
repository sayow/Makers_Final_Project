package com.belle_musica

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class AlbumsPageTest {

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
}