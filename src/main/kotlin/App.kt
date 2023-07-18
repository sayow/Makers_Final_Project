
import org.http4k.core.*
import org.http4k.core.cookie.cookie
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.lens.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

//
//fun checkAuthenticated(contexts: RequestContexts) = Filter { next ->
//    {
//        if (contexts[it].get<Int>("user_id") == null) {
//            Response(Status.FOUND).header("Location", "/sessions/new")
//        } else {
//            next(it)
//        }
//    }
//}
//
//fun authenticateRequestFromSession(contexts: RequestContexts) = Filter { next ->
//    {
//        val cookie = it.cookie("acebook_session_id")
//        val sessionId = cookie?.value
//
//        if (sessionId != null) {
//            val userId = sessionCache.get(sessionId) ?: null
//            if (userId != null) {
//                val currentUser = database.sequenceOf(Users).find { it.id eq userId }
//
//                contexts[it]["user_id"] = userId
//                contexts[it]["user"] = currentUser
//            }
//        }
//
//        next(it)
//    }
//}
//
//
//
//fun appHttpHandler(contexts: RequestContexts): HttpHandler =
//    ServerFilters.InitialiseRequestContext(contexts)
//        .then(ServerFilters.CatchLensFailure(::failResponse))
//        .then(DebuggingFilters.PrintRequest())
//        .then(authenticateRequestFromSession(contexts))
//        .then(app(contexts))