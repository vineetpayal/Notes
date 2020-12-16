package com.orcus.notes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.orcus.notes.data.Note

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

    //Make sure to use List not ArrayList in the return type
    //Room does not allow or maybe does not support to use ArrayList
    //So user List<T> instead if ArrayList<T>
    @Query("SELECT * FROM NOTES_TABLE ORDER BY primaryKey DESC")
    fun getAllNotes(): LiveData<List<Note>>
}