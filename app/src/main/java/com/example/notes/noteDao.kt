package com.example.notes

import androidx.room.Dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface noteDao {

    @Insert
    suspend fun insertnote(notesEntity: notes_entity)

    @Update
    suspend fun updatenote(notesEntity: notes_entity)

    @Delete
    suspend fun deletenote(notesEntity: notes_entity)

    @Query("SELECT * FROM `notes_table`")
    fun fetchnotes() :Flow<List<notes_entity>>

    @Query("SELECT * FROM `notes_table` WHERE id = :id")
     fun applyupdatenote(id : Int) : Flow<notes_entity>

}