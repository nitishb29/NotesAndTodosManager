package com.nb.mynotolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nb.mynotolist.db.database.NotoDatabase
import com.nb.mynotolist.db.entities.NoteEntity
import com.nb.mynotolist.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = NotoDatabase.invoke(application).getNoteDao()
    private val noteRepo = NoteRepository(noteDao)

    val getAllNotes : StateFlow<List<NoteEntity>> = noteRepo.getAllNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun insertNote(note : NoteEntity){
        viewModelScope.launch {
            noteRepo.insertNote(note)
        }
    }
    fun updateNote(note : NoteEntity){
        viewModelScope.launch {
            noteRepo.updateNote(note)
        }
    }
    fun deleteNote(note : NoteEntity){
        viewModelScope.launch {
            noteRepo.deleteNote(note)
        }
    }
    fun searchNote(query: String?) : StateFlow<List<NoteEntity>> = noteRepo.searchNote(query).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
}