package com.belle_musica

import com.belleMusica.schemas.Users
import database
import org.junit.jupiter.api.AfterEach
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

    companion object {
        @JvmStatic
        @BeforeAll
        fun restoreDb(): Unit {
            database.delete(Users) { it.id eq it.id }
        }
    }
    @BeforeEach
    fun driver(){
//        val chromeOptions = ChromeOptions()
//        chromeOptions.addArguments("--headless")
//        chromeOptions.addArguments("--disable-gpu")
        driver = ChromeDriver()
        driver.get("http://localhost:9999/users/new")
    }
    @AfterEach
    fun closeBrowser(){
        driver.quit()
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
            .filter { it.email eq "mail@mail2.com" }
            .firstOrNull()
        assert(ourUser != null)
        println("AQUIIIII" + ourUser)

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
        assert(ourUser == 1)
    }

    @Test
    fun `User Not created because of existing email`() {
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail@mail2.com")
        Thread.sleep(2000)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        Thread.sleep(2000)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed2")
        Thread.sleep(2000)
        val submit = driver.findElement(By.id("submitbutton")).click()
        Thread.sleep(2000)
        val ourUser = database.sequenceOf(Users).toList().size
        assert(ourUser == 1)
    }

    @Test
    fun `User Not created because of incorrect password`() {
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail1@mail2.com")
        Thread.sleep(2000)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("123")
        Thread.sleep(2000)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed")
        Thread.sleep(2000)
        val submit = driver.findElement(By.id("submitbutton")).click()
        Thread.sleep(2000)
        val ourUser = database.sequenceOf(Users).toList().size
        assert(ourUser == 1)
    }

    @Test
    fun `User Not created because of existing username`() {
        val email: Unit = driver.findElement(By.id("emailinput")).sendKeys("mail1@mail2.com")
        Thread.sleep(2000)
        val password: Unit = driver.findElement(By.id("passwordinput")).sendKeys("Password@123")
        Thread.sleep(2000)
        val username: Unit = driver.findElement(By.id("usernameinput")).sendKeys("Ahmed")
        Thread.sleep(2000)
        val submit = driver.findElement(By.id("submitbutton")).click()
        Thread.sleep(2000)
        val ourUser = database.sequenceOf(Users).toList().size

        assert(ourUser == 1)
    }
}
