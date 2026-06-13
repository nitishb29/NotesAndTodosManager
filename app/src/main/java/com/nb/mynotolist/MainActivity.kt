package com.nb.mynotolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.nb.mynotolist.routes.NotoRouter
import com.nb.mynotolist.viewmodel.NoteViewModel
import com.nb.mynotolist.viewmodel.NoteViewModelFactory
import com.nb.mynotolist.viewmodel.TodoViewModel
import com.nb.mynotolist.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(application)
    }
    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationController = rememberNavController()
            NotoRouter(navigationController, noteViewModel, todoViewModel)
        }
    }
}