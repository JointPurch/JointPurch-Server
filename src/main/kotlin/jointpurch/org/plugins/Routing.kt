package jointpurch.org.plugins

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import jointpurch.org.RoomManager
import jointpurch.org.UserManager
import jointpurch.org.data.Item
import jointpurch.org.data.Room
import jointpurch.org.data.User
import java.util.UUID

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
                if (UserManager.users.none { it.id == user.id || it.login == user.login }){
                    UserManager.addUser(user)
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        route("/room"){
            get{
                call.respond(RoomManager.rooms)
            }
            authenticate("auth-basic") {
                get("/my"){
                    val userId = call.principal<UserIdPrincipal>()!!.name
                    val answer = RoomManager.rooms.filter { it.users.any { user -> user.id == userId } } // Room by userId
                    call.respond(answer)
                }

                post("/register"){
                    val room = call.receive<Room>()
                    if (RoomManager.rooms.none { it.id == room.id }){
                        RoomManager.addRoom(room)
                        call.respond(HttpStatusCode.OK)
                    }else{
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                post("/invite"){
                    val response = call.receive<HashMap<String, String>>()
                    val roomId: String = response["room_id"]!!
                    val userId: String = response["user_id"]!!

                    if(RoomManager.rooms.any { it.id == roomId } && // Room exists
                            UserManager.users.any { it.id == userId } && // User exists
                            RoomManager.rooms.find { it.id == roomId }!!.users.none { it.id == userId }){ // User not in room
                        RoomManager.rooms.find { it.id == roomId }!!.users.add(UserManager.users.find { it.id == userId }!!) // Find room and add user
                    }else{
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    RoomManager.dump()
                    call.respond(HttpStatusCode.OK)
                }

                post("/newitem"){
                    val response = call.receive<HashMap<String, String>>()
                    val roomId: String = response["room_id"]!!
                    val itemName: String = response["item_name"]!!

                    val newItem = Item(
                        UUID.randomUUID().toString(),
                        itemName,
                        false
                    )

                    if(RoomManager.rooms.any { it.id == roomId }){ // Room exists
                        RoomManager.rooms.find { it.id == roomId }!!.items.add(newItem) // Add new item
                    }else{
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    RoomManager.dump()
                    call.respond(HttpStatusCode.OK)
                }

                post("/checkitem"){
                    val response = call.receive<HashMap<String, String>>()
                    val roomId: String = response["room_id"]!!
                    val itemId: String = response["item_id"]!!

                    if(RoomManager.rooms.any { it.id == roomId } && // Room exists
                            RoomManager.rooms.find { it.id == roomId }!!.items.any { it.id == itemId }){ // Item exists in the room
                        val item = RoomManager.rooms.find { it.id == roomId }!!.items.find { it.id == itemId }!! // Get item in the room
                        item.is_checked = !item.is_checked
                    }else{
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    RoomManager.dump()
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
