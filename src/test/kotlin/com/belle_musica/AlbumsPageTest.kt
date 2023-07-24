package com.belle_musica
import com.belleMusica.schemas.Users
import database
import okhttp3.OkHttpClient
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.core.cookie.cookie
import org.http4k.lens.WebForm
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.openqa.selenium.WebDriver
import org.riversun.okhttp3.OkHttp3CookieHelper
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.junit.jupiter.api.TestInstance
import org.ktorm.dsl.deleteAll
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.openqa.selenium.chrome.ChromeOptions
import sessionCache

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlbumsPageTest {

    private lateinit var driver: WebDriver

    @BeforeAll
    fun setup() {
        database.deleteAll(Users)
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("--disable-gpu")
        driver = ChromeDriver()
        driver.get("http://localhost:9999/")
        val registerButton = driver.findElement(By.id("register-btn")).click()
        Thread.sleep(2000)
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail@mail2.com")
        Thread.sleep(2000)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        Thread.sleep(2000)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed")
        Thread.sleep(2000)
        val submit = driver.findElement(By.id("submitbutton")).click()
        Thread.sleep(2000)

        val createdUser = database.sequenceOf(Users)
            .filter {it.email eq "mail@mail2.com"}
            .firstOrNull()
        assert(createdUser!= null)
        val emailLogin: Unit = driver.findElement(By.id("email")).sendKeys("mail@mail2.com")
        Thread.sleep(2000)
        val passwordLogin: Unit = driver.findElement(By.id("password")).sendKeys("Password@123")
        Thread.sleep(2000)
        val submitLogin = driver.findElement(By.id("submit")).click()
        Thread.sleep(2000)
    }

   @AfterAll
   fun driverShutDown(){
       driver.quit()

   }


    @Test
    fun `like an album and test if it goes to the first place`() {
        val likeButton = driver.findElement(By.id("likeButton>Music")).click()
        Thread.sleep(2000)
        val albumElements = driver.findElements(By.className("album"))
        val firstElement = albumElements[0].text
        assert(firstElement.contains(">Music"))
        Thread.sleep(2000)
    }


    @Test
    fun `test logout the user and check going back to the main page`() {
        val logoutButton = driver.findElement(By.id("signout")).click()
        Thread.sleep(2000)
        val albumElements = driver.findElements(By.className("likedAlbumCard"))
        val firstElement = albumElements[0].text
        assert(firstElement.contains(">Music"))
    }

}