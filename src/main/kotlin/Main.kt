

import com.belleMusica.handlers.getHomePage
import io.github.reactivecircus.cache4k.Cache
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.HandlebarsTemplates


val templateRenderer = HandlebarsTemplates().HotReload("src/main/resources")


val app: HttpHandler = routes(
    "/" bind Method.GET to {request :Request ->
        getHomePage(request)

    }
)
fun main() {

//    val port = Environment.port()
    val server = app.asServer(Undertow(9000)).start()

    println("Server started on " + server.port())
}