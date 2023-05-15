package jointpurch.org.data

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    var id: String,
    var name: String,
    var users: List<User>,
    var items: List<Item>
)
