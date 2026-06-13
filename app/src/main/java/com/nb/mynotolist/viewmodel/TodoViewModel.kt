package com.nb.mynotolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nb.mynotolist.db.database.NotoDatabase
import com.nb.mynotolist.db.entities.TodoEntity
import com.nb.mynotolist.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = NotoDatabase.invoke(application).getTodoDao()
    private val todoRepo = TodoRepository(todoDao)

    val getAllItems : StateFlow<List<TodoEntity>> = todoRepo.getAllItems().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

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
    fun searchItem(query : String?) : StateFlow<List<TodoEntity>> = todoRepo.searchItem(query).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

}