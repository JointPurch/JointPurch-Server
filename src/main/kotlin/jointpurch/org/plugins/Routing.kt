package jointpurch.org.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import jointpurch.org.RoomManager
import jointpurch.org.UserManager
import jointpurch.org.data.Room
import jointpurch.org.data.User

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/user"){
            get{
                call.respond(UserManager.users)
            }
            post("/register"){
                val user = call.receive<User>()
                UserManager.addUser(user)
                call.respond(user)
            }
        }

        route("/room"){
            get{
                call.respond(RoomManager.rooms)
            }
            post("/register"){
                val room = call.receive<Room>()
                RoomManager.addRoom(room)
                call.respond(room)
            }

            authenticate("auth-basic") {
                get("/my"){
                    val userName = call.principal<UserIdPrincipal>()!!.name
                    call.respond(userName)
                }
            }
        }
    }
}
