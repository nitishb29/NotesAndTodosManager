package com.nb.mynotolist.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.nb.mynotolist.db.entities.TodoEntity
import com.nb.mynotolist.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TodoScreen(
    navigationController: NavHostController,
    todoViewModel: TodoViewModel
) {
    val tasks by todoViewModel.getAllItems.collectAsState()

    val backgroundGradient = listOf(
        Color.Black.copy(0.6f),
        Color.Black.copy(0.8f),
        Color.Black
    )
    var taskToEdit by remember { mutableStateOf<TodoEntity?>(null) }
    var openEditorSheet by remember { mutableStateOf(false) }
    var enableCheckboxToDelete by remember { mutableStateOf(false) }
    var listOfItemsToDelete by remember { mutableStateOf(listOf<TodoEntity>()) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openEditorSheet = true
                    taskToEdit = null
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
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(
                        painter = painterResource(R.drawable.checklist_add_icon),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 5.dp, end = 10.dp)
                    )
                    Text(
                        text = "Add List",
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
                navigationController = navigationController,
                enableCheckboxToDelete = enableCheckboxToDelete,
                listOfItemsToDelete = listOfItemsToDelete,
                listOfNotesToDelete = null,
                todoViewModel = todoViewModel,
                noteViewModel = null,
                todoQueryResult = {},
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
                    text = "Todo Lists",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                    color = Color.White,
                    textDecoration = TextDecoration.Underline
                )
            }
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No Tasks Added",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = tasks) { task ->
                        var isTaskSelected by remember { mutableStateOf(false) }
                        var isTaskCompleted by remember {
                            mutableStateOf(
                                task.isCompleted ?: false
                            )
                        }
                        var isExpanded by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
                                .fillMaxWidth()
                                .animateContentSize(animationSpec = tween(durationMillis = 300))
                                .clip(RoundedCornerShape(24.dp))
                                .combinedClickable(
                                    onClick = {isExpanded = !isExpanded},
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AnimatedVisibility(visible = enableCheckboxToDelete) {
                                    Column(modifier = Modifier.padding(horizontal = 7.dp)) {
                                        Checkbox(
                                            checked = isTaskSelected,
                                            onCheckedChange = {
                                                isTaskSelected = isTaskSelected.not()
                                                listOfItemsToDelete =
                                                    listOfItemsToDelete.plus(task)
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Color.White.copy(0.8f),
                                                checkmarkColor = Color.Black,
                                                uncheckedColor = Color.Gray
                                            )
                                        )
                                    }
                                }
                                Column(modifier = Modifier) {
                                    RadioButton(
                                        selected = isTaskCompleted,
                                        onClick = {
                                            isTaskCompleted = isTaskCompleted.not()
                                            task.let { existingTask ->
                                                todoViewModel.updateItem(
                                                    existingTask.copy(
                                                        isCompleted = existingTask.isCompleted?.not()
                                                    )
                                                )
                                            }
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color.White.copy(0.8f),
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                }
                                Column(modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp)) {
                                    Text(
                                        task.description,
                                        modifier = Modifier,
                                        fontFamily = FontFamily(Font(R.font.unbounded_medium)),
                                        color = if (task.isCompleted == true) Color.Gray.copy(
                                            0.8f
                                        ) else Color.White.copy(
                                            0.8f
                                        ),
                                        fontSize = 18.sp,
                                        textDecoration = if (task.isCompleted == true) TextDecoration.LineThrough else TextDecoration.None,
                                        softWrap = true,
                                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Column(
                                    modifier = Modifier,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier.width(70.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .padding(end = 10.dp)
                                                .clickable(onClick = {
                                                    openEditorSheet = true
                                                    taskToEdit = task
                                                })
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .clickable(
                                                    onClick = {
                                                        todoViewModel.deleteItem(task)
                                                    }
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (openEditorSheet) {
            AddTodoModalSheet(
                onSave = { newDescription ->
                    if (taskToEdit == null) {
                        todoViewModel.insertItem(
                            TodoEntity(
                                description = newDescription,
                                isCompleted = false
                            )
                        )
                    } else {
                        taskToEdit?.let { existingTask ->
                            todoViewModel.updateItem(existingTask.copy(description = newDescription))
                        }
                    }
                    openEditorSheet = false
                    taskToEdit = null
                },
                onCancel = { openEditorSheet = false },
                todoItem = taskToEdit
            )
        }
    }
}