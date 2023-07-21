package com.belle_musica

import com.belleMusica.schemas.Users
import database
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterUserTest {

    private lateinit var driver: WebDriver

    @BeforeAll
    fun silentMood() {
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("--disable-gpu")
        driver = ChromeDriver(chromeOptions)
        driver.get("http://localhost:9999/users/new")
    }


    @Test
    fun `Successfully created a new user`() {
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail@mail2.com")
        Thread.sleep(2000)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        Thread.sleep(2000)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed")
        Thread.sleep(2000)
        val submit = driver.findElement(By.id("submitbutton")).click()
        Thread.sleep(2000)

        val ourUser = database.sequenceOf(Users)
            .filter { it.id eq 1 }
            .firstOrNull()
        if (ourUser != null) {
            assert(ourUser.email == "mail@mail2.com")
        }
    }
    @Test
    fun `User Not created because of incorrect email`() {
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail")
        Thread.sleep(2000)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        Thread.sleep(2000)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed")
        Thread.sleep(2000)
        val submit = driver.findElement(By.id("submitbutton")).click()
        Thread.sleep(2000)

        val ourUser = database.sequenceOf(Users).toList().size
        println(database.sequenceOf(Users).toList())
        println(ourUser)
        assert(ourUser == 1)

    }

    companion object {
        @BeforeEach
        fun restoreDb(): Unit {
            database.delete(Users) { it.id eq it.id }
        }
    }

}
