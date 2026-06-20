package com.nb.mynotolist.routes

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
    NavHost(
        navigationController,
        startDestination = NotoScreens.NotesScreen.screenName
    ) {
        composable(
            route = NotoScreens.NotesScreen.screenName,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            }
        ) {
            NotesScreen(navigationController, noteViewModel)
        }
        composable(NotoScreens.TodosScreen.screenName,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            }
        ) {
            TodoScreen(navigationController, todoViewModel)
        }
    }
}