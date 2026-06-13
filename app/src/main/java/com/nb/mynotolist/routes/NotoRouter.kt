package com.nb.mynotolist.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nb.mynotolist.screens.NotesScreen
import com.nb.mynotolist.screens.TodoScreen
import com.nb.mynotolist.viewmodel.NoteViewModel
import com.nb.mynotolist.viewmodel.TodoViewModel

@Composable
fun NotoRouter(
    navigationController: NavHostController,
    noteViewModel: NoteViewModel,
    todoViewModel: TodoViewModel
) {
    NavHost(navigationController,
        startDestination = NotoScreens.NotesScreen.screenName){
        composable(route = NotoScreens.NotesScreen.screenName){
            NotesScreen( navigationController, noteViewModel)
        }
        composable(NotoScreens.TodosScreen.screenName){
            TodoScreen(navigationController, todoViewModel)
        }
    }
}