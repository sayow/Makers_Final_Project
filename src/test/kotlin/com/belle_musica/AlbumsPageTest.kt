package com.belle_musica

import com.belleMusica.schemas.Users
import database
import okhttp3.OkHttpClient
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.lens.WebForm
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.openqa.selenium.WebDriver
import org.riversun.okhttp3.OkHttp3CookieHelper
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.junit.jupiter.api.TestInstance
import org.openqa.selenium.chrome.ChromeOptions

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
    fun `test clicking on the Like button`() {
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("--disable-gpu")
        driver = ChromeDriver(chromeOptions)
        driver.get("http://localhost:9999/users/new")

        //Register a new user
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail@mail2.com")
        Thread.sleep(2000)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        Thread.sleep(2000)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed")
        Thread.sleep(2000)
        val submit = driver.findElement(By.id("submitbutton")).click()
        Thread.sleep(2000)

        //Login in the user created
        val emailLogin: Unit = driver.findElement(By.id("email")).sendKeys("mail@mail2.com")
        Thread.sleep(2000)
        val passwordLogin: Unit = driver.findElement(By.id("password")).sendKeys("Password@123")
        Thread.sleep(2000)
        val submitLogin = driver.findElement(By.id("submit")).click()
        Thread.sleep(2000)

        //Check the sorting by likes
        val likeButton = driver.findElement(By.id("likeButton>Music")).click()

        //Assertions
        val albumElements = driver.findElements(By.className("album"))
        val firstElement = albumElements[0].text
        assert(firstElement.contains(">Music"))

        // Close the browser after the test is finished
        driver.quit()
    }


    companion object {
        @JvmStatic
        @BeforeAll
        fun restoreDb(): Unit {
            database.delete(Users) { it.id eq it.id }
        }
    }


}














