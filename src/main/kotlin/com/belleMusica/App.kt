import com.belleMusica.Environment
import com.belleMusica.handlers.getHomePage
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.HandlebarsTemplates
import org.ktorm.database.Database


val templateRenderer = HandlebarsTemplates().HotReload("src/main/resources")
val database = Database.connect("jdbc:postgresql://localhost:5432/${Environment.databaseName()}")

val app: HttpHandler = routes(
    "/" bind Method.GET to { request : Request ->
        getHomePage(request)
    }
)