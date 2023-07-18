import com.belleMusica.Environment
import com.belleMusica.handlers.getHomePage
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.HandlebarsTemplates
import org.ktorm.database.Database



fun main() {

    val port = Environment.port()
    val server = app.asServer(Undertow(port)).start()
    println(database.name)
    println("Server started on " + server.port())
}