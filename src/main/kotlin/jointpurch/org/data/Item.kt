package jointpurch.org.data

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    var id: String,
    var name: String,
    var is_checked: Boolean
)
