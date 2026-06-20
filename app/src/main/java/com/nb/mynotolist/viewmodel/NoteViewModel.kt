package com.nb.mynotolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nb.mynotolist.db.database.NotoDatabase
import com.nb.mynotolist.db.entities.NoteEntity
import com.nb.mynotolist.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = NotoDatabase.invoke(application).getNoteDao()
    private val noteRepo = NoteRepository(noteDao)
    val searchQuery = MutableStateFlow("")

    val notesState: StateFlow<List<NoteEntity>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                noteRepo.getAllNotes() // If search is empty, emit everything
            } else {
                noteRepo.searchNote(query) // Otherwise, emit filtered records
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

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
}