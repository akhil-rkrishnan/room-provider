package com.akhil.room

import akhil.room.utils.loge
import com.akhil.room.utils.Constants.AUTHORITY
import com.akhil.room.utils.Constants.CODE_ALL_ITEM
import com.akhil.room.utils.Constants.CODE_SINGLE_ITEM
import com.akhil.room.utils.Constants.TABLE_NAME
import com.akhil.room.utils.DBUtils
import akhil.room.utils.toRoomItem
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.content.UriMatcher.NO_MATCH
import android.database.Cursor
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DatabaseProvider : ContentProvider() {
    private var cursor: Cursor? = null
    private val uriMatcher = UriMatcher(NO_MATCH)
    private val scope = CoroutineScope(SupervisorJob())

    init {
        uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/single", CODE_SINGLE_ITEM)
        uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/all", CODE_ALL_ITEM)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        if (uriMatcher.match(uri) == -1)
            return null
        uri.loge()
        when (uri.lastPathSegment) {
            "single" -> {

                selectionArgs?.first()?.toIntOrNull()?.let {
                    getData(it)
                }
            }
            "all" -> {
                getAllItem()
            }
        }
        cursor?.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            CODE_SINGLE_ITEM -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
            CODE_ALL_ITEM-> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            CODE_SINGLE_ITEM -> {
                val roomItem = values.toRoomItem()
                insertData(uri, roomItem)
            }
        }
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    private fun getData(id: Int) {
        cursor = DBUtils.database?.noteDao?.getItem(id)
    }

    private fun getAllItem() {
        cursor = DBUtils.database?.noteDao?.getAllNotes()
    }

    private fun insertData(uri: Uri, roomItem: RoomItem) {
        scope.launch {
            val id = DBUtils.database?.noteDao?.insert(roomItem) ?: -1
            context?.contentResolver?.notifyChange(uri, null)
            ContentUris.withAppendedId(uri, id)
        }
    }

    private fun insertDummies() {
        repeat(10) {
            val item = RoomItem(
                title = "Title: ${(0..9).random()}",
                content = "Content ${(100..200).random()}"
            )
            scope.launch {
                delay(500)
                DBUtils.database?.noteDao?.insert(item)
            }
        }
    }
}
