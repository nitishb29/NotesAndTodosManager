package com.nb.mynotolist.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nb.mynotolist.db.entities.NoteEntity
import com.nb.mynotolist.db.entities.TodoEntity

@Database(entities = [NoteEntity::class, TodoEntity::class], version = 1)
abstract class NotoDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDAO
    abstract fun getTodoDao(): TodoDAO

    companion object {
        @Volatile
        private var INSTANCE: NotoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: createDatabase(context).also {
                INSTANCE = it
            }
        }

        fun createDatabase(context: Context) =
            Room.databaseBuilder(
                        context,
                        NotoDatabase::class.java,
                        "noto_db"
                    ).fallbackToDestructiveMigration(false)
                .addMigrations()
                .build()
    }

}