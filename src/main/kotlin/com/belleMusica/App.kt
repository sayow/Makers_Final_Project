import com.belleMusica.Environment
import com.belleMusica.handlers.*
import org.dotenv.vault.dotenvVault

import com.belleMusica.schemas.Users
import org.http4k.core.*
import org.http4k.core.cookie.cookie
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.lens.*
import org.http4k.routing.ResourceLoader


import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.template.HandlebarsTemplates
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf

val dotenv = dotenvVault()
val requiredEmailField = FormField.nonEmptyString().required("email")
val requiredPasswordField = FormField.nonEmptyString().required("password")
val requiredUsernameField = FormField.nonEmptyString().required("username")
val requiredSearchUserInputField = FormField.nonEmptyString().required("search_user_input")

val requiredLoginCredentialsLens = Body.webForm(
    Validator.Strict,
    requiredEmailField,
    requiredPasswordField
).toLens()

val requiredSignupFormLens = Body.webForm(
    Validator.Strict,
    requiredEmailField,
    requiredPasswordField,
    requiredUsernameField
).toLens()

val requiredSearchUserLens = Body.webForm(
    Validator.Strict,
    requiredSearchUserInputField
).toLens()

fun checkAuthenticated(contexts: RequestContexts) = Filter { next ->
    {
        if (contexts[it].get<Int>("user_id") == null) {
            Response(Status.FOUND).header("Location", "/sessions/new")
        } else {
            next(it)
        }
    }
}

fun authenticateRequestFromSession(contexts: RequestContexts) = Filter { next ->
    {
        val cookie = it.cookie("belle_musica_session_id")
        val sessionId = cookie?.value

        if (sessionId != null) {
            val userId = sessionCache.get(sessionId) ?: null
            if (userId != null) {
                val currentUser = database.sequenceOf(Users).find { it.id eq userId }

                contexts[it]["user_id"] = userId
                contexts[it]["user"] = currentUser
            }
        }

        next(it)
    }
}

fun app(contexts: RequestContexts) = routes(
    "/" bind Method.GET to getLandingPage(contexts),
    "/users" bind routes(
        "/new" bind Method.GET to newUserHandler(),
        "/" bind Method.POST to ServerFilters.CatchLensFailure(::signupFailResponse).then(createUserHandler())
    ),

    "/sessions" bind routes(
        "/new" bind Method.GET to newSessionHandler(),
        "/" bind Method.POST to ServerFilters.CatchLensFailure(::loginFailResponse).then(createSessionHandler()),
        "/clear" bind Method.GET to destroySessionHandler()
    ),
   "/albums" bind Method.GET to checkAuthenticated(contexts).then(getAlbumPage(contexts)),

    "/static" bind static(ResourceLoader.Directory(dotenv["STATIC_FOLDER"])),
    "/static-photos" bind static(ResourceLoader.Directory(dotenv["UPLOAD_FOLDER"])),

    "/like/{id}" bind Method.GET to {request: Request ->
        val idParamLens = Path.string().of ( "id")
        val id = idParamLens(request)
        toggleLikeOnAlbumPage(contexts, request, id)
    },
    "/profile" bind routes(
        "/" bind Method.GET to checkAuthenticated(contexts).then(viewProfile(contexts)),
        "/updateProfilePicture" bind Method.POST to checkAuthenticated(contexts).then(updateProfilePicture(contexts)),
        "/unlike/{id}" bind Method.GET to { request: Request ->
            val idParamLens = Path.string().of("id")
            val id = idParamLens(request)
            unlikeAlbumOnProfile(contexts, request, id)
        }
    ),
    "/likeOnSelectedProfile/{id}&{userId}" bind Method.GET to { request: Request ->
        val idParamLens = Path.string().of("id")
        val id = idParamLens(request)
        val userIdParamLens = Path.string().of("userId")
        val userId = userIdParamLens(request).toInt()
        toggleLikeOnSelectedProfile(contexts, request, id, userId)
    },
    "/{id}" bind Method.GET to checkAuthenticated(contexts).then{request: Request ->
        val idParamLens = Path.string().of ( "id")
        val id = idParamLens(request).toInt()
        getProfileWithId(contexts, request, id)
    },
    "/searchUser" bind Method.POST to checkAuthenticated(contexts).then(searchUser(contexts)),
    "follow/{id}" bind Method.GET to {request: Request ->
        val idParamLens = Path.string().of ( "id")
        val userId = idParamLens(request).toInt()
        toggleFollowUser(contexts, request, userId)
    }
)
fun failResponse (failure: LensFailure) =
    Response(Status.BAD_REQUEST).body("Invalid parameters 1")

fun appHttpHandler(contexts: RequestContexts): HttpHandler =
    ServerFilters.InitialiseRequestContext(contexts)
        .then(ServerFilters.CatchLensFailure(::failResponse))
        .then(DebuggingFilters.PrintRequest())
        .then(authenticateRequestFromSession(contexts))
        .then(app(contexts))
