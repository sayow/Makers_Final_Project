package com.belleMusica.handlers

import com.belleMusica.entities.*
import com.belleMusica.schemas.Followers
import com.belleMusica.schemas.Likes
import com.belleMusica.schemas.Likes.userId
import com.belleMusica.schemas.Users
import com.belleMusica.viewmodel.ProfileViewModel
import com.belleMusica.viewmodel.SearchUserResultsViewModel
import database
import org.http4k.core.*
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import templateRenderer
import java.io.File
import java.util.*
import org.dotenv.vault.dotenvVault
import org.ktorm.dsl.*
import org.ktorm.entity.add
import requiredSearchUserInputField
import requiredSearchUserLens

val dotenv = dotenvVault()

fun viewProfile(contexts: RequestContexts):  HttpHandler = { request: Request ->
    val currentUser: User? = contexts[request]["user"]
     if (currentUser != null) {
         val likedAlbums = getLikedAlbums(currentUser)
         val followedUsers = getFollowedUsers(currentUser.id)
         val viewModel = ProfileViewModel(currentUser, likedAlbums, followedUsers)
         Response(Status.OK)
             .body(templateRenderer(viewModel))
     } else {
         Response(Status.BAD_REQUEST)
     }
}

fun getLikedAlbums(currentUser: User): List<Album>{
    val likedList = database.sequenceOf(Likes).filter { userId eq currentUser.id }.toList()
    val likedAlbumsIdList: List<String> = likedList.map { obj ->
        obj.albumId
    }
    return albumList.filter {
        it.id in likedAlbumsIdList }
}

fun getFollowedUsers(currentUserId: Int): MutableList<User>{
    //.filter { followerId eq currentUserId }.toList()
    val followedUsers = mutableListOf<User>()
    database
        .from(Followers)
        .innerJoin(Users, on = Followers.followerId eq Users.id)
        .select (Users.id, Users.username, Users.email, Users.encryptedPassword, Users.profilePicture )
        .where (Followers.followerId eq currentUserId)
        .map { row ->
                val user = User {
                    id = row[Users.id]!!
                    username = row[Users.username].toString()
                    email = row[Users.email].toString()
                    encryptedPassword = row[Users.encryptedPassword].toString()
                    profilePicture = row[Users.profilePicture].toString()
                }
                followedUsers.add(user)
            }
    return followedUsers
}

fun updateProfilePicture(contexts: RequestContexts): HttpHandler = {request: Request ->
    val receivedForm = MultipartFormBody.from(request)
    val file = receivedForm.file("profile_picture")
    val filename = file?.filename
    val contentType = file?.contentType
    val inputStream = file?.content
    if (filename != null && contentType != null && inputStream != null) {
        val uniqueFilename = UUID.randomUUID().toString()
        val extension = filename.substringAfterLast(".", "")
        val savedFilename = "$uniqueFilename.$extension"
        //TODO: Remember to change/hide the absolute path before merging

        val uploadDirectory = dotenv["UPLOAD_FOLDER"]

        val savedFile = File(uploadDirectory, savedFilename)
        inputStream.use { input ->
            savedFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val pictureLink = "static-photos/$savedFilename"

        val currentUser: User? = contexts[request]["user"]
        database.update(Users) {
            set(it.profilePicture, pictureLink)
            if (currentUser != null) {
                where {
                    it.id eq currentUser.id
                }
            }
        }
        Response(Status.SEE_OTHER)
            .header("Location", "/profile")
    } else {
        Response(Status.BAD_REQUEST).body("No picture uploaded")
    }
}

fun searchUser(contexts: RequestContexts): HttpHandler = { request: Request ->
    val currentUser: User? = contexts[request]["user"]
    val searchUserForm = requiredSearchUserLens(request)
    val searchUserInput = requiredSearchUserInputField(searchUserForm)
    val users = database.sequenceOf(Users).filter { it.username like "%$searchUserInput%" }.toList()
    println(users)
    val message = if(users.isEmpty()) "No user was matching with your search" else null
    val resultsViewModel = SearchUserResultsViewModel(currentUser, users, message)
    Response(Status.OK).body(templateRenderer(resultsViewModel))
}

fun followUser(contexts: RequestContexts, request: Request, userId: Int): Response {
    val currentUser: User? = contexts[request]["user"]
    if (currentUser != null) {
        if (!isUserFollowedByUser(currentUser.id, userId)) {
            val newFollower = Follower {
                followerId = currentUser.id
                followedUserId = userId
            }
            database.sequenceOf(Followers).add(newFollower)
        }
    }
    return Response(Status.SEE_OTHER)
        .header("Location", "/profile")
}

fun isUserFollowedByUser(followerId: Int, followedUserId: Int): Boolean {
    return database.sequenceOf(Followers)
        .filter{(it.followerId eq followerId) and (it.followedUserId eq followedUserId)}
        .toList()
        .isNotEmpty()
}