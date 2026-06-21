package com.nb.mynotolist.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nb.mynotolist.R
import com.nb.mynotolist.db.entities.NoteEntity
import com.nb.mynotolist.db.entities.TodoEntity
import com.nb.mynotolist.routes.NotoScreens
import com.nb.mynotolist.ui.theme.buttonColor
import com.nb.mynotolist.ui.theme.cardColor
import com.nb.mynotolist.viewmodel.NoteViewModel
import com.nb.mynotolist.viewmodel.TodoViewModel

@Composable
fun NotoBottomBar(
    navigationController: NavHostController,
    enableCheckboxToDelete: Boolean,
    listOfItemsToDelete: List<TodoEntity>?,
    listOfNotesToDelete: List<NoteEntity>?,
    todoViewModel: TodoViewModel?,
    noteViewModel: NoteViewModel?,
    showSearchBar: (Boolean) -> Unit,
    modifier: Modifier,
    openEditor: (Pair<Boolean, Pair<TodoEntity?, NoteEntity?>>) -> Unit
) {
    var enableSearch by remember { mutableStateOf(false) }
    val iconSize = 25.dp
    val textSize = 15.sp
    var openEditorSheet by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
            .clip(CircleShape)
            .background(buttonColor)
            .border(2.dp, Color.White.copy(0.2f), CircleShape)
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardColor)
                .blur(20.dp)
                .border(1.dp, Color.White.copy(0.1f))
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = enableCheckboxToDelete,
                transitionSpec = {
                    ContentTransform(
                        targetContentEnter = slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            tween(500)
                        ),
                        initialContentExit = fadeOut(
                            tween(350)
                        )
                    )
                }
            ) { isCheckboxEnabled ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = {
                                if(navigationController.currentDestination?.route != NotoScreens.NotesScreen.screenName) {
                                    navigationController.navigate(NotoScreens.NotesScreen.screenName) {
                                        popUpTo(0)
                                    }
                                }
                            }, onClickLabel = "Notes"),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.notes_icon),
                            contentDescription = "Notes Icon",
                            tint = Color.White,
                            modifier = Modifier
                                .size(iconSize)
                        )
                        Text(
                            "Notes",
                            fontSize = textSize,
                            fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                            color = Color.White,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = {
                                if(navigationController.currentDestination?.route != NotoScreens.TodosScreen.screenName) {
                                    navigationController.navigate(NotoScreens.TodosScreen.screenName) {
                                        popUpTo(0)
                                    }
                                }
                            }, onClickLabel = "Todo"),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.checklists_icon),
                            contentDescription = "checklist Icon",
                            tint = Color.White,
                            modifier = Modifier
                                .size(iconSize)
                        )
                        Text(
                            "Tasks",
                            fontSize = textSize,
                            fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                            color = Color.White,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                onClick = {
                                    enableSearch = enableSearch.not()
                                    showSearchBar(enableSearch)
                                },
                                onClickLabel = "Notes"
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.White,
                            modifier = Modifier
                                .size(iconSize)
                        )
                        Text(
                            "Search",
                            fontSize = textSize,
                            fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                            color = Color.White,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    if (isCheckboxEnabled) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(onClick = {
                                    if (todoViewModel != null && listOfItemsToDelete != null) {
                                        listOfItemsToDelete.forEach { item ->
                                            todoViewModel.deleteItem(item)
                                        }
                                    }
                                    if (noteViewModel != null && listOfNotesToDelete != null) {
                                        listOfNotesToDelete.forEach { note ->
                                            noteViewModel.deleteNote(note)
                                        }
                                    }
                                }, onClickLabel = "Delete Items"),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Icon",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(iconSize)
                            )
                            Text(
                                "Delete",
                                fontSize = textSize,
                                fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                                color = Color.White,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(onClick = { openEditor(Pair(openEditorSheet.not(), Pair(null, null))) }),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(iconSize)
                            )
                            Text(
                                "New",
                                fontSize = textSize,
                                fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                                color = Color.White,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}