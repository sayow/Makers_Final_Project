package com.belle_musica

import com.belleMusica.handlers.dotenv
import com.belleMusica.schemas.Users
import database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.ktorm.dsl.deleteAll
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class FollowersBehaviourTest {

    private lateinit var driver: WebDriver


    @Test
    fun `click the search bar, search for a friend to follow and friend is added to following`(){
        fromRegisterToSearch()
        val follow = driver.findElement(By.id("follow_btn")).click()
        val resultPageText = driver.findElement(By.tagName("p")).text
        assert(resultPageText.contains("user"))
    }
    @Test
    fun `click the search bar, find a friend, follow them, unfollow them`(){
        fromRegisterToSearch()
        val follow = driver.findElement(By.id("follow_btn")).click()
        val unfollow = driver.findElement(By.id("unfollow-button")).click()
        val followedUsers = driver.findElements(By.className("followedUsers"))
        assert(followedUsers.size==0)
    }

    @Test
    fun `search for a friend, visit their profile page`(){
        fromRegisterToSearch()
        val profilePicture = driver.findElement(By.className("profile_picture_search")).click()
        val visitedUserUserName = driver.findElement(By.className("visitedUserName")).text
        assert(visitedUserUserName.contains("user"))
    }
    fun fromRegisterToSearch(){
        driverSetup()
        setupAutoUser("mail@mail.com", "Password@123", "user")
        setupAutoUser("mail2@mail.com", "Password@123", "user2")
        loginUser("mail2@mail.com", "Password@123")
        val clickOnProfileButton = driver.findElement(By.id("profile_picture")).click()
        val searchInput = driver.findElement(By.name("search_user_input")).sendKeys("user")
        val submit = driver.findElement(By.id("SearchBtn")).click()
        val resultPageText = driver.findElement(By.tagName("p")).text
    }

    fun driverSetup(){
        database.deleteAll(Users)
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("--disable-gpu")
        driver = ChromeDriver(chromeOptions)
        driver.get("http://localhost:9999/")
    }
    fun setupAutoUser(emailInput: String, passwordInput: String, usernameInput: String ) {
        val registerButton = driver.findElement(By.id("register-btn")).click()
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys(emailInput)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys(passwordInput)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys(usernameInput)
        val submit = driver.findElement(By.id("submitbutton")).click()
    }

    fun loginUser(emailInput: String, passwordInput: String){
        val emailLogin: Unit = driver.findElement(By.id("email")).sendKeys(emailInput)
        val passwordLogin: Unit = driver.findElement(By.id("password")).sendKeys(passwordInput)
        val submitLogin = driver.findElement(By.id("submit")).click()
    }

    fun driverShutDown(){
        driver.quit()
    }

}