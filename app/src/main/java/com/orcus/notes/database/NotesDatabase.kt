package com.orcus.notes.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.orcus.notes.dao.NoteDao
import com.orcus.notes.data.Note
import com.orcus.notes.ui.HomeFragment.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao


    companion object {

        @Volatile
        var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NotesDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "word_database"
                )
                    .addCallback(NoteDatabaseCallbacks(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

}

private class NoteDatabaseCallbacks(
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.d(TAG, "onCreate: Created the database ")
        NotesDatabase.INSTANCE?.let { database ->
            scope.launch {
                populateDbForTheFirstTime(database.noteDao())
            }
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)

    }

    suspend fun populateDbForTheFirstTime(noteDao: NoteDao) {
        val note = Note("This is testing note so dont take it seriously okay")
        val note1 = Note("It is not a real note so dont take it seriously okay")


        noteDao.insert(note)
        noteDao.insert(note1)
    }

}
