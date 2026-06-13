package com.nb.mynotolist.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nb.mynotolist.R
import com.nb.mynotolist.db.entities.NoteEntity
import com.nb.mynotolist.viewmodel.NoteViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    navigationController: NavHostController,
    noteViewModel: NoteViewModel
) {
    val notes by noteViewModel.getAllNotes.collectAsState()
    var notesFromQuery = listOf<NoteEntity>()
    var allNotes = notesFromQuery.ifEmpty { notes }
    val backgroundGradient = listOf(
        Color.Black.copy(0.6f),
        Color.Black.copy(0.8f),
        Color.Black
    )
    var enableCheckboxToDelete by remember { mutableStateOf(false) }
    var listOfNotesToDelete by remember { mutableStateOf(listOf<NoteEntity>()) }
    var openEditorSheet by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<NoteEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openEditorSheet = true
                    noteToEdit = null
                },
                containerColor = Color.DarkGray,
                contentColor = Color.White,
                modifier = Modifier
                    .padding(bottom = 35.dp, end = 15.dp)
                    .scale(1.25f)
            ) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        painter = painterResource(R.drawable.note_add_icon),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 5.dp, end = 10.dp)
                    )
                    Text(
                        text = "Add Note",
                        fontFamily = FontFamily(Font(R.font.unbounded_medium)),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(backgroundGradient))
                .padding(innerPadding)
        ) {
            NotoTileBar(
                navigationController,
                enableCheckboxToDelete,
                listOfItemsToDelete = null,
                listOfNotesToDelete,
                noteViewModel = noteViewModel,
                todoViewModel = null,
                todoQueryResult = { notes -> notes?.let { notesFromQuery =
                    notesFromQuery.plus(notes) as List<NoteEntity>
                }
                },
                notesQueryResult = {}
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Notes",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                    color = Color.White,
                    textDecoration = TextDecoration.Underline
                )
            }
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No Notes Added",
                        fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                        fontSize = 24.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(top = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    items(items = notes) { note ->
                        var isExpanded by remember { mutableStateOf(false) }
                        var isTaskSelected by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                .fillMaxWidth()
                                .animateContentSize(animationSpec = tween(durationMillis = 300))
                                .clip(RoundedCornerShape(24.dp))
                                .combinedClickable(
                                    onClick = { isExpanded = !isExpanded },
                                    onLongClick = {
                                        enableCheckboxToDelete = enableCheckboxToDelete.not()
                                    }
                                ),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Black.copy(0.1f),
                                contentColor = Color.White
                            ),
                            border = BorderStroke(2.dp, Color.White.copy(0.2f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 10.dp),
                                verticalArrangement = Arrangement.SpaceAround,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateContentSize(animationSpec = tween(durationMillis = 300)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AnimatedVisibility(visible = enableCheckboxToDelete) {
                                        Checkbox(
                                            checked = isTaskSelected,
                                            onCheckedChange = {
                                                isTaskSelected = isTaskSelected.not()
                                                if (isTaskSelected) {
                                                    listOfNotesToDelete =
                                                        listOfNotesToDelete.plus(note)
                                                }
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Color.White.copy(0.8f),
                                                checkmarkColor = Color.Black,
                                                uncheckedColor = Color.Gray
                                            ),
                                            modifier = Modifier.padding(start = 20.dp)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        Text(
                                            note.title,
                                            modifier = Modifier
                                                .padding(horizontal = 25.dp),
                                            fontFamily = FontFamily(Font(R.font.unbounded_medium)),
                                            fontSize = 22.sp,
                                        )
                                        Text(
                                            note.description,
                                            modifier = Modifier
                                                .padding(horizontal = 25.dp)
                                                .padding(bottom = 15.dp),
                                            fontFamily = FontFamily(Font(R.font.unbounded_medium)),
                                            color = Color.LightGray,
                                            softWrap = true,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 18.sp,
                                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (openEditorSheet) {
                AddNoteModalSheet(
                    note = noteToEdit,
                    onCancel = { openEditorSheet = false },
                    onSave = { newValues ->
                        if (noteToEdit == null) {
                            noteViewModel.insertNote(
                                NoteEntity(
                                    title = newValues.first,
                                    description = newValues.second
                                )
                            )
                        } else {
                            noteToEdit?.let { existingNote ->
                                noteViewModel.updateNote(
                                    existingNote.copy(
                                        title = newValues.first,
                                        description = newValues.second
                                    )
                                )
                            }
                        }
                        openEditorSheet = false
                        noteToEdit = null
                    }
                )
            }
        }
    }
}