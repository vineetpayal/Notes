package com.orcus.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes_table")
data class Note(
        @ColumnInfo(name = "note")
        var content: String,

        @PrimaryKey(autoGenerate = true)
        val primaryKey: Int = 0
)


