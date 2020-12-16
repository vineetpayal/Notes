package com.orcus.notes.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.orcus.notes.Repositiory.Repository
import com.orcus.notes.data.Note
import kotlinx.coroutines.launch

class NotesViewModel(var repository: Repository) : ViewModel() {

    var allNotes: LiveData<List<Note>> = repository.allNotes

    var isNoteInserted: Boolean = false
    var isNoteRemoved: Boolean = false
    var isNoteUpdated: Boolean = false


    init {

    }

    fun insert(note: Note) {
        viewModelScope.launch {
            repository.insert(note)
        }
        isNoteInserted = true
    }


    fun update(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
        isNoteUpdated = true
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
        isNoteRemoved = true
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
        isNoteRemoved = true
    }

    class NotesViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
