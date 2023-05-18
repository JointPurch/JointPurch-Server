package jointpurch.org

import jointpurch.org.data.User
import java.util.UUID

object UserManager {
    var users: MutableList<User> = mutableListOf<User>()

    fun addUser(user: User){
        users.add(user)
    }

    fun addUser(login: String, passwordHash: String?){
        users.add(
            User(
                UUID.randomUUID().toString(),
                login,
                passwordHash
            )
        )
    }
}