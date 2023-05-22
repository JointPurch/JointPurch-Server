package jointpurch.org

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.http.ContentType.Application.Json
import jointpurch.org.data.Room
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.*

object RoomManager {
    const val FILE_PATH = "json/rooms.json"
    var rooms: MutableList<Room> = mutableListOf<Room>()

    init {
        load()
    }

    fun addRoom(room: Room){
        rooms.add(room)
        dump()
    }

    fun addRoom(name: String): Room{
        val newRoom = Room(
            UUID.randomUUID().toString(),
            name,
            mutableListOf(),
            mutableListOf()
        )
        rooms.add(newRoom)
        dump()
        return newRoom
    }

    fun dump(){
        try {
            PrintWriter(FileWriter(FILE_PATH)).use {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gson.toJson(rooms)
                it.write(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun load(){
        try {
            val json = File(FILE_PATH).readText()
            val typeToken = object : TypeToken<MutableList<Room>>() {}.type
            rooms = Gson().fromJson<MutableList<Room>>(json, typeToken)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}