package com.nb.mynotolist.repository

import com.nb.mynotolist.db.database.TodoDAO
import com.nb.mynotolist.db.entities.TodoEntity

class TodoRepository(private val todoDAO: TodoDAO) {
    suspend fun insertItem(item: TodoEntity) = todoDAO.insertItem(item)
    suspend fun updateItem(item: TodoEntity) = todoDAO.updateItem(item)
    suspend fun deleteItem(item: TodoEntity) = todoDAO.deleteItem(item)

    fun getAllItems() = todoDAO.getAllItems()
    fun searchItem(query: String?) = todoDAO.searchItem(query)
}