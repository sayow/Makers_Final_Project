import com.belleMusica.Environment
import com.belleMusica.handlers.*
import com.belleMusica.schemas.Users
import org.http4k.core.*
import org.http4k.core.cookie.cookie
import org.http4k.filter.ServerFilters
import org.http4k.lens.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.HandlebarsTemplates
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf


val requiredEmailField = FormField.nonEmptyString().required("email")
val requiredPasswordField = FormField.nonEmptyString().required("password")
val requiredUsernameField = FormField.nonEmptyString().required("username")

val requiredSignupFormLens = Body.webForm(
    Validator.Strict,
    requiredEmailField,
    requiredPasswordField,
    requiredUsernameField
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
        val cookie = it.cookie("acebook_session_id")
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

val app: HttpHandler = routes(
    "/" bind Method.GET to { request : Request ->
        getHomePage(request)

    },
    "/users" bind routes(
        "/new" bind Method.GET to newUserHandler(),
        "/" bind Method.POST to ServerFilters.CatchLensFailure(::signupFailResponse).then(createUserHandler())
    ),

    "/sessions" bind routes(
        "/new" bind Method.GET to newSessionHandler(),
        "/" bind Method.POST to ServerFilters.CatchLensFailure(::loginFailResponse).then(createSessionHandler()),
        "/clear" bind Method.GET to destroySessionHandler()
    ),
)