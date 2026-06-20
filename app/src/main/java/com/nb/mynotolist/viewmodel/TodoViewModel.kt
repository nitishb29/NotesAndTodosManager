package com.nb.mynotolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nb.mynotolist.db.database.NotoDatabase
import com.nb.mynotolist.db.entities.TodoEntity
import com.nb.mynotolist.repository.TodoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = NotoDatabase.invoke(application).getTodoDao()
    private val todoRepo = TodoRepository(todoDao)
    val searchQuery = MutableStateFlow("")
    val todosState: StateFlow<List<TodoEntity>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                todoRepo.getAllItems() // If search is empty, emit everything
            } else {
                todoRepo.searchItem(query) // Otherwise, emit filtered records
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

    fun insertItem(item : TodoEntity){
        viewModelScope.launch {
            todoRepo.insertItem(item)
        }
    }
    fun updateItem(item : TodoEntity){
        viewModelScope.launch {
            todoRepo.updateItem(item)
        }
    }
    fun deleteItem(item : TodoEntity){
        viewModelScope.launch {
            todoRepo.deleteItem(item)
        }
    }
}