package com.belle_musica
import com.belleMusica.handlers.dotenv
import com.belleMusica.schemas.Users
import database
import org.http4k.core.*
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.junit.jupiter.api.TestInstance
import org.ktorm.dsl.deleteAll
import org.openqa.selenium.chrome.ChromeOptions


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AlbumsPageTest {

    private lateinit var driver: WebDriver


    @Test
    fun `Test like album`() {
        setupAutoUser()
        likeAnAlbum()
        val albumElements = driver.findElements(By.className("album"))
        val firstElement = albumElements[0].text
        assert(firstElement.contains(">Music"))
        driverShutDown()
    }


    @Test
    fun `Test user's liked album appears on profile page`() {
        setupAutoUser()
        likeAnAlbum()
        val clickOnProfileButton = driver.findElement(By.id("profile_picture")).click()
        Thread.sleep(1000)
        val likedAlbumElements = driver.findElements(By.className("album"))
        val firstElement = likedAlbumElements[0].text
        assert(firstElement.contains(">Music"))
        Thread.sleep(1000)
        driverShutDown()
    }
    @Test
    fun `Test upload profile picture and check if displayed`() {
        setupAutoUser()
        val clickOnProfileButton = driver.findElement(By.id("profile_picture")).click()
        val fileInput = driver.findElement(By.id("profilePictureInput"))
        val filePath = dotenv["TEST_PHOTO"]
        fileInput.sendKeys(filePath)
        driver.findElement(By.id("photoForm")).submit()
        val uploadedImage = driver.findElement(By.id("profilePicture"))
        assert(uploadedImage.isDisplayed)
    }

    @Test
    fun `Test logout and check rendering the most popular albums`() {
        setupAutoUser()
        likeAnAlbum()
        val logoutButton = driver.findElement(By.id("signout")).click()
        val albumElements = driver.findElements(By.className("likedAlbumCard"))
        val firstElement = albumElements[0].text
        assert(firstElement.contains(">Music"))
        driverShutDown()
    }





    fun likeAnAlbum(){
        val likeButton = driver.findElement(By.id("likeButton>Music")).click()
        Thread.sleep(1000)
    }

    @Test
    fun testUserBehaviour(){
        database.deleteAll(Users)
        driver = ChromeDriver()
        driver.get("http://localhost:9999/")
        val registerButton = driver.findElement(By.id("register-btn")).click()
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail@mail2.com")
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed")
        val submit = driver.findElement(By.id("submitbutton")).click()
        Thread.sleep(2000)
        val emailLogin: Unit = driver.findElement(By.id("email")).sendKeys("mail@mail2.com")
        val passwordLogin: Unit = driver.findElement(By.id("password")).sendKeys("Password@123")
        val submitLogin = driver.findElement(By.id("submit")).click()
        val likeButton = driver.findElement(By.id("likeButton>Music")).click()
        Thread.sleep(2000)
        val clickOnProfileButton = driver.findElement(By.id("profile_picture")).click()
        Thread.sleep(2000)
        val likedAlbumElements = driver.findElements(By.className("album"))
        val firstElementLiked = likedAlbumElements[0].text
        assert(firstElementLiked.contains(">Music"))
        Thread.sleep(2000)
        val fileInput = driver.findElement(By.id("profilePictureInput"))
        val filePath = dotenv["TEST_PHOTO"]
        fileInput.sendKeys(filePath)
        driver.findElement(By.id("photoForm")).submit()
        val uploadedImage = driver.findElement(By.id("profilePicture"))
        assert(uploadedImage.isDisplayed)
        Thread.sleep(2000)


        val logoutButton = driver.findElement(By.id("signout")).click()
        val albumElements = driver.findElements(By.className("likedAlbumCard"))
        val firstElement = albumElements[0].text
        assert(firstElement.contains(">Music"))
        Thread.sleep(1500)
        driverShutDown()
    }


    fun setupAutoUser() {
        database.deleteAll(Users)
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("--disable-gpu")
        driver = ChromeDriver(chromeOptions)
        driver.get("http://localhost:9999/")
        val registerButton = driver.findElement(By.id("register-btn")).click()
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail@mail2.com")
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Sara")
        val submit = driver.findElement(By.id("submitbutton")).click()
        val emailLogin: Unit = driver.findElement(By.id("email")).sendKeys("mail@mail2.com")
        val passwordLogin: Unit = driver.findElement(By.id("password")).sendKeys("Password@123")
        val submitLogin = driver.findElement(By.id("submit")).click()
    }

    fun driverShutDown(){
        driver.quit()
    }
}

