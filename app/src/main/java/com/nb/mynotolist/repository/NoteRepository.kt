package com.nb.mynotolist.repository

import com.nb.mynotolist.db.database.NoteDAO
import com.nb.mynotolist.db.entities.NoteEntity

class NoteRepository(private val noteDAO: NoteDAO) {
    suspend fun insertNote(note: NoteEntity) = noteDAO.insertNote(note)
    suspend fun updateNote(note: NoteEntity) = noteDAO.updateNote(note)
    suspend fun deleteNote(note: NoteEntity) = noteDAO.deleteNote(note)

    fun getAllNotes() = noteDAO.getAllNotes()
    fun searchNote(query: String?) = noteDAO.searchNote(query)
}