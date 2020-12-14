package com.orcus.notes.Repositiory

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orcus.notes.dao.NoteDao
import com.orcus.notes.data.Note
import kotlinx.coroutines.flow.Flow

class Repository(private val dao: NoteDao) {

    val allNotes: Flow<ArrayList<Note>> = dao.getAllNotes()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Note) {
        dao.insert(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(note: Note){
        dao.update(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(note: Note){
        dao.delete(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll(){
        dao.deleteAll()
    }

}