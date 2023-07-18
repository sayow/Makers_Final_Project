import com.belleMusica.Environment
import com.belleMusica.handlers.*
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.lens.Path
import org.http4k.lens.int
import org.http4k.lens.string
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.HandlebarsTemplates
import org.ktorm.database.Database

val templateRenderer = HandlebarsTemplates().HotReload("src/main/resources")
val database = Database.connect("jdbc:postgresql://localhost:5432/${Environment.databaseName()}")

val app: HttpHandler = routes(
    "/albums" bind Method.GET to { request : Request ->
        getAlbumPage(request)
    },
    "/like/{id}" bind Method.GET to {request: Request ->
        println("IN ROUTE like")
        val idParamLens = Path.string().of ( "id")
        val id =idParamLens(request)
        likeAlbum(request, id)
    }
)