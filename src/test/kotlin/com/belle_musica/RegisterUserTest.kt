package com.belle_musica

import com.belleMusica.schemas.Users
import database
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterUserTest {

    private lateinit var driver: WebDriver


    @AfterEach
    fun closeBrowser(){
        driver.quit()
    }
    @BeforeEach
    fun resetDb(){
        database.deleteAll(Users)
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("--disable-gpu")
        driver = ChromeDriver(chromeOptions)
        driver.get("http://localhost:9999/users/new")
    }

    @Test
    fun `Successfully created a new user`() {
        setupUser("mail@mail2.com","Password@123","Sara")
        val ourUser = database.sequenceOf(Users)
            .filter { it.email eq "mail@mail2.com" }
            .firstOrNull()
        assert(ourUser != null)


    }

    @Test
    fun `User Not created because of incorrect email`() {
        setupUser("mail","Password@123","Sara")
        val resultPageText = driver.findElement(By.tagName("body")).text
        assert(resultPageText.contains("The email is incorrect, choose a valid email."))
    }

    @Test
    fun `User Not created because of existing email`() {
        setupUser("mail1@mail2.com","Password@123","Sara")
        val registerButton = driver.findElement(By.id("register-btn")).click()
        setupUser("mail1@mail2.com","Password@123","Sara")
        val ourUser = database.sequenceOf(Users).toList().size
        assert(ourUser == 1)
        val resultPageText = driver.findElement(By.tagName("body")).text
        assert(resultPageText.contains("The user already exists, choose another email."))
    }

    @Test
    fun `User Not created because of incorrect password`() {
        setupUser("mail1@mail2.com","123","Sara")
        val ourUser = database.sequenceOf(Users).toList().size
        assert(ourUser == 0)
        val resultPageText = driver.findElement(By.tagName("body")).text
        assert(resultPageText.contains("The password should contain at least one digit, one lowercase letter, one uppercase letter, one special character and have a minimum length of 8 characters."))
    }

    @Test
    fun `User Not created because of existing username`() {
        setupUser("mail1@mail2.com","Password@123","Sara")
        val registerButton = driver.findElement(By.id("register-btn")).click()
        setupUser("mail1@mail3.com","Password@123","Sara")
        val resultPageText = driver.findElement(By.tagName("body")).text
        assert(resultPageText.contains("The user already exists, choose another username."))

    }
    fun setupUser(userMail: String,userPass: String, userUsername: String ){
        val registerButton = driver.findElement(By.id("register-btn")).click()
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys(userMail)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys(userPass)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys(userUsername)
        val submit = driver.findElement(By.id("submitbutton")).click()
    }

}
