package com.akhil.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val noteDao: RoomDao
}