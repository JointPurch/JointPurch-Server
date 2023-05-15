package jointpurch.org.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import jointpurch.org.UserManager

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/users"){
            get{
                call.respond(UserManager.users)
            }
        }
        route("/rooms"){

        }
    }
}
