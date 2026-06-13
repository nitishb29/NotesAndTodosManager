package com.nb.mynotolist.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String
)
