package com.orcus.notes.utils

import android.app.Application
import com.orcus.notes.Repositiory.Repository
import com.orcus.notes.database.NotesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NoteApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts

}