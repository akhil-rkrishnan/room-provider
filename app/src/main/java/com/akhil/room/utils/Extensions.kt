package akhil.room.utils

import com.akhil.room.RoomItem
import android.content.ContentValues
import android.util.Log
import com.akhil.room.utils.Constants


fun ContentValues?.toRoomItem(): RoomItem {
    return RoomItem(
        id = this?.getAsInteger(Constants.KEY_ID) ?: -1,
        title = this?.getAsString(Constants.KEY_TITLE) ?: "value to room title null",
        content = this?.getAsString(Constants.KEY_CONTENT) ?: "value to room content null"
    )
}

fun Any?.loge(tag: String = "AkhilRoom") {
    Log.e(tag, "${this?.toString()}")
}