package com.nb.mynotolist.routes

sealed class NotoScreens(
    val screenName : String
){
    data object NotesScreen : NotoScreens("notes")
    data object TodosScreen : NotoScreens("todos")
}