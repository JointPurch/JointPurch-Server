package jointpurch.org.plugins

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
                UserManager.addUser(user)
                call.respond(user)
            }
        }

        route("/room"){
            get{
                call.respond(RoomManager.rooms)
            }
            authenticate("auth-basic") {
                get("/my"){
                    val userName = call.principal<UserIdPrincipal>()!!.name
                    call.respond(userName)
                }

                post("/register"){
                    val room = call.receive<Room>()
                    RoomManager.addRoom(room)
                    call.respond(room)
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
                        call.respond("Bad data.")
                    }
                    call.respond("User invited.")
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
                        call.respond("Bad data.")
                    }
                    call.respond("Item added.")
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
                        call.respond("Bad data.")
                    }
                    call.respond("Item toggled.")
                }
            }
        }
    }
}
