package com.nb.mynotolist.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.nb.mynotolist.viewmodel.NoteViewModel
import com.nb.mynotolist.viewmodel.TodoViewModel

@Composable
fun NotoTileBar(
    navigationController: NavHostController,
    enableCheckboxToDelete: Boolean,
    listOfItemsToDelete: List<TodoEntity>?,
    listOfNotesToDelete: List<NoteEntity>?,
    todoViewModel: TodoViewModel?,
    noteViewModel: NoteViewModel?,
    todoQueryResult: (List<TodoEntity>?) -> Unit,
    notesQueryResult: (List<NoteEntity>?) -> Unit
) {
    val isNotesModelEmpty = noteViewModel?.getAllNotes == null
    val isTodosModelEmpty = noteViewModel?.getAllNotes == null
    var enableSearch by remember { mutableStateOf(false) }
    var valueToSearch by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = {
                            navigationController.navigate(NotoScreens.NotesScreen.screenName) {
                                popUpTo(0)
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
                            .size(30.dp)
                    )
                    Text(
                        "Notes",
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                        color = Color.White,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = {
                            navigationController.navigate(NotoScreens.TodosScreen.screenName) {
                                popUpTo(0)
                            }
                        }, onClickLabel = "Notes"),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.checklists_icon),
                        contentDescription = "checklist Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        "Tasks",
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                        color = Color.White,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(
                            onClick = { enableSearch = enableSearch.not() },
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
                            .size(30.dp)
                    )
                    Text(
                        "Search",
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                        color = Color.White,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                AnimatedVisibility(
                    visible = enableCheckboxToDelete and !(isNotesModelEmpty and isTodosModelEmpty)
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
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
                                .size(30.dp)
                        )
                        Text(
                            "Delete",
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                            color = Color.White,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
            AnimatedVisibility(visible = enableSearch) {
                OutlinedTextField(
                    value = valueToSearch,
                    onValueChange = { valueToSearch = it },
                    placeholder = {
                        Text(
                            text = "Search",
                            fontFamily = FontFamily(Font(R.font.unbounded_semibold))
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.DarkGray,
                        unfocusedPlaceholderColor = Color.White.copy(0.5f),
                        focusedPlaceholderColor = Color.White,
                        focusedIndicatorColor = Color.White.copy(0.3f),
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (todoViewModel != null) {
                                    todoQueryResult(todoViewModel.searchItem(valueToSearch).value)
                                } else if (noteViewModel != null) {
                                    notesQueryResult(noteViewModel.searchNote(valueToSearch).value)
                                }
                            },
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color.White,
                            )
                        }
                    }
                )
            }
        }
    }
}