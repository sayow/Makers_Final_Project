package com.belle_musica

import com.belleMusica.handlers.albumList
import com.belleMusica.handlers.getSpotifyAlbums
import com.belleMusica.schemas.Users
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import database
import io.github.bonigarcia.wdm.WebDriverManager
import okhttp3.OkHttpClient
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.http4k.lens.WebForm
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.openqa.selenium.WebDriver
import org.riversun.okhttp3.OkHttp3CookieHelper
import requiredLoginCredentialsLens
import requiredSignupFormLens
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.devtools.DevTools
import org.junit.jupiter.api.TestInstance
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlbumsPageTest {

    private val cookieHelper = OkHttp3CookieHelper()
    val client = OkHttp(OkHttpClient().newBuilder().cookieJar(cookieHelper.cookieJar()).build())
    val signUpForm = WebForm(
        mapOf(
            "email" to listOf("email@email.com"),
            "password" to listOf("Password@123"),
            "username" to listOf("user")
        )
    )
    val loginForm = WebForm(
        mapOf(
            "email" to listOf("email@email.com"),
            "password" to listOf("Password@123"),
        )
    )

    private lateinit var driver: WebDriver


    @Test
    fun `Get the home page`() {

        val response: Response = client(
            Request(Method.GET, "http://localhost:9999/albums")
        )

        assertThat(response, hasStatus(Status.OK))
        assertThat(
            response,
            hasBody(containsSubstring("Albums"))
        )
    }
    @Test
    fun `test api response`(){
        getSpotifyAlbums()
        assert(albumList.size == 100)
    }

    @Test
    fun `test the signUp`(){

        val response: Response = client(
            Request(Method.POST,"http://localhost:9999/users").with(
                requiredSignupFormLens of signUpForm
            )
        )
        assertThat(response, hasStatus(Status.OK))

    }

    @Test
    fun `test the login`(){
        val response: Response = client(
            Request(Method.POST,"http://localhost:9999/sessions").with(
                requiredLoginCredentialsLens of loginForm
            )
        )
        assert(response.bodyString().contains("Albums"))
    }

    @Test
    fun `test clicking on the Like button`() {

//        System.setProperty("webdriver.chrome.driver", "/Users/mmu4265/Desktop/driver/chromedriver")
        driver = ChromeDriver()
        driver.get("http://localhost:9999/users/new")

        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail@mail2.com")
        Thread.sleep(2000)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        Thread.sleep(2000)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed")
        Thread.sleep(2000)
        val submit = driver.findElement(By.id("submitbutton")).click()

    }







        companion object {
        @JvmStatic
        @BeforeAll
        fun restoreDb(): Unit {
            database.delete(Users) { it.id eq it.id }


        }
    }


}