package com.orcus.notes.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.orcus.notes.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM NOTES_TABLE ")
    suspend fun deleteAll()

    @Query("SELECT * FROM NOTES_TABLE ORDER BY primaryKey DESC")
    fun getAllNotes() : Flow<ArrayList<Note>>
}