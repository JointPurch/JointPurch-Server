package jointpurch.org

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import jointpurch.org.data.Room
import jointpurch.org.data.User
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.UUID

object UserManager {
    const val FILE_PATH = "json/users.json"
    var users: MutableList<User> = mutableListOf<User>()

    init {
        load()
    }

    fun addUser(user: User){
        users.add(user)
        dump()
    }

    fun addUser(login: String, passwordHash: String?){
        users.add(
            User(
                UUID.randomUUID().toString(),
                login,
                passwordHash
            )
        )
        dump()
    }

    private fun dump(){
        try {
            PrintWriter(FileWriter(FILE_PATH)).use {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gson.toJson(UserManager.users)
                it.write(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun load(){
        try {
            val json = File(FILE_PATH).readText()
            val typeToken = object : TypeToken<MutableList<User>>() {}.type
            users = Gson().fromJson<MutableList<User>>(json, typeToken)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}