package jointpurch.org

import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import jointpurch.org.data.User
import jointpurch.org.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {}
        json()
    }
    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                if (UserManager.users.any {
                        it.id == credentials.name &&
                        it.passwordHash == credentials.password
                }){
                    println("LOGIN SUCCEED")
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
    configureRouting()

    UserManager.addUser("Styopa", "gjiorj")
    UserManager.addUser("Vasya", "niuguhdi")
    UserManager.addUser("Petya", "gjiorj")
    RoomManager.addRoom("room123")
}
