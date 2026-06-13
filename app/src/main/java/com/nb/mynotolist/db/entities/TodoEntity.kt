package com.nb.mynotolist.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_todo")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val isCompleted: Boolean ? = false
)
