package com.orcus.notes.lifecycle

import androidx.lifecycle.*
import com.orcus.notes.Repositiory.Repository
import com.orcus.notes.data.Note
import kotlinx.coroutines.launch

class NotesViewModel(var repository: Repository) : ViewModel() {

    var allNotes: LiveData<ArrayList<Note>> = repository.allNotes.asLiveData()

    fun insert(note: Note) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }


    fun update(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

}
