package jointpurch.org

import com.google.gson.Gson
import io.ktor.http.ContentType.Application.Json
import jointpurch.org.data.Room
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.*

object RoomManager {
    const val FILE_PATH = "/json/rooms.json"
    var rooms: MutableList<Room> = mutableListOf<Room>()

    fun addRoom(room: Room){
        rooms.add(room)
    }

    fun addRoom(name: String): Room{
        val newRoom = Room(
            UUID.randomUUID().toString(),
            name,
            listOf(),
            listOf()
        )
        rooms.add(newRoom)
        return newRoom
    }

    fun dump(){
        try {
            PrintWriter(FileWriter(FILE_PATH)).use {
                val gson = Gson()
                val jsonString = gson.toJson(rooms)
                it.write(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(){

    }
}