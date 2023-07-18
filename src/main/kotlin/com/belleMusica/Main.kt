import com.belleMusica.Environment
import org.http4k.server.Undertow
import org.http4k.server.asServer


fun main() {
    val port = Environment.port()
    val server = app.asServer(Undertow(port)).start()
    println(database.name)
    println("Server started on " + server.port())
}