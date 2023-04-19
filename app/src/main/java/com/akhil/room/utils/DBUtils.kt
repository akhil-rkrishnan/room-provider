package com.akhil.room.utils

import com.akhil.room.AppDatabase
import android.content.Context
import androidx.room.Room

object DBUtils {
    var database: AppDatabase? = null
    private val lock = Object()
    fun initializeRoomDatabase(context: Context) {
        synchronized(lock) {
            if (database == null) {
                database = Room.databaseBuilder(context, AppDatabase::class.java, "remoteRoomDB")
                    .build()
            }
        }
    }

    fun closeDB() {
        database?.close()
    }
}