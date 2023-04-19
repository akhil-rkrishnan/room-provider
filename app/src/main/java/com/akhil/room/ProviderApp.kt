package com.akhil.room

import akhil.room.utils.loge
import android.app.Application
import com.akhil.room.utils.DBUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProviderApp : Application() {
    private val scope = CoroutineScope(SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        DBUtils.initializeRoomDatabase(this)
        insertDummies()
    }

    private fun insertDummies() {
        scope.launch {
            repeat(10) {
                val item = RoomItem(
                    title = "Title: ${(0..9).random()}",
                    content = "Content ${(100..200).random()}"
                )
                delay(500)
                DBUtils.database?.noteDao?.insert(item)
            }
            val cursor = DBUtils.database?.noteDao?.getAllNotes()
            cursor?.count.loge()
        }
    }
}