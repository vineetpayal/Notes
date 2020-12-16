package com.orcus.notes.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Notes_table")
data class Note(
    @ColumnInfo(name = "note")
    var content: String,

    val timeCreated: Long = System.currentTimeMillis(),

    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 0

) : Parcelable


