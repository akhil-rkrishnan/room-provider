package com.akhil.room

import com.akhil.room.utils.Constants.TABLE_NAME
import android.database.Cursor
import androidx.room.*

@Dao
interface RoomDao {
    @Insert
    suspend fun insert(note: RoomItem): Long

    @Update
    suspend fun update(note: RoomItem)

    @Delete
    suspend fun delete(note: RoomItem)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllNotes(): Cursor

    @Query("SELECT * FROM $TABLE_NAME where id=:ID")
    fun getItem(ID: Int): Cursor
}