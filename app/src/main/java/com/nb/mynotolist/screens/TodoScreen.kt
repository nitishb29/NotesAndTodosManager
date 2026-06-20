package com.nb.mynotolist.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nb.mynotolist.R
import com.nb.mynotolist.db.entities.TodoEntity
import com.nb.mynotolist.ui.theme.bgColor
import com.nb.mynotolist.ui.theme.cardColor
import com.nb.mynotolist.viewmodel.TodoViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TodoScreen(
    navigationController: NavHostController,
    todoViewModel: TodoViewModel
) {
    val tasks by todoViewModel.todosState.collectAsState()
    var taskToEdit by remember { mutableStateOf<TodoEntity?>(null) }
    var openEditorSheet by remember { mutableStateOf(false) }
    var enableCheckboxToDelete by remember { mutableStateOf(false) }
    var listOfItemsToDelete by remember { mutableStateOf(listOf<TodoEntity>()) }
    val valueToSearch by todoViewModel.searchQuery.collectAsState()
    var showSearchBar by remember { mutableStateOf(false) }

    //Scrolling Bottom Bar
    var isBottomBarVisible by remember { mutableStateOf(true) }
    val animatedYOffset by animateFloatAsState(
        targetValue = if (isBottomBarVisible) 0f else 300f, // Shifts down 300px to hide cleanly
        label = "BottomBarScrollAnimation"
    )

    // 3. Intercept scrolling gestures from the list
    val scrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -10) {
                    isBottomBarVisible = false
                } else if (available.y > 20) {
                    isBottomBarVisible = true
                }
                return Offset.Zero
            }
        }
    }
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(innerPadding)
                .navigationBarsPadding()
                .nestedScroll(scrollConnection)
        ) {
            Box(modifier = Modifier.align(Alignment.TopCenter)) {
                Column(
                    Modifier.animateContentSize(
                        tween(500)
                    )
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AnimatedContent(
                            targetState = showSearchBar,
                            transitionSpec = {
                                ContentTransform(
                                    targetContentEnter = slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                        tween(500)
                                    ),
                                    initialContentExit = slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                        tween(250)
                                    )
                                )
                            }
                        ) { isSearchBarVisible ->
                            if (isSearchBarVisible) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.1.dp, horizontal = 20.dp)
                                ) {
                                    OutlinedTextField(
                                        value = valueToSearch,
                                        onValueChange = { newValue ->
                                            todoViewModel.onSearchQueryChanged(newValue)
                                        },
                                        placeholder = {
                                            Text(
                                                text = "Todo",
                                                fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        },
                                        singleLine = true,
                                        shape = CircleShape,
                                        modifier = Modifier
                                            .fillMaxWidth(),
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
                                        leadingIcon = {
                                            IconButton(
                                                onClick = {},
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "Search Icon",
                                                    tint = Color.White.copy(0.8f),
                                                )
                                            }
                                        },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = { todoViewModel.onSearchQueryChanged("") },
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Clear,
                                                    contentDescription = "Clear Icon",
                                                    tint = Color.White.copy(0.8f),
                                                )
                                            }
                                        }
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 23.1.dp, horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = "Todo Lists",
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                                        color = Color.White
                                    )
                                }
                            }
                        }
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
                                val infiniteTransition = rememberInfiniteTransition()
                                val selectedCardBorderColor by infiniteTransition.animateColor(
                                    initialValue = Color.Gray.copy(0.4f),
                                    targetValue = Color.White.copy(0.4f),
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(500, easing = FastOutSlowInEasing),
                                        repeatMode = RepeatMode.Reverse
                                    )
                                )
                                var isExpanded by remember { mutableStateOf(false) }
                                val borderColor =
                                    if (isTaskSelected) selectedCardBorderColor else Color.White.copy(
                                        0.2f
                                    )
                                val textSize = if (isExpanded) 18.sp else 15.sp
                                Card(
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
                                        .fillMaxWidth()
                                        .animateContentSize(
                                            animationSpec = tween(durationMillis = 300)
                                        )
                                        .clip(RoundedCornerShape(24.dp))
                                        .combinedClickable(
                                            onClick = {
                                                if (enableCheckboxToDelete) {
                                                    isTaskSelected = isTaskSelected.not()
                                                } else {
                                                    isExpanded = !isExpanded
                                                }
                                            },
                                            onLongClick = {
                                                enableCheckboxToDelete =
                                                    enableCheckboxToDelete.not()
                                                isTaskSelected = isTaskSelected.not()
                                                if (isTaskSelected) {
                                                    listOfItemsToDelete =
                                                        listOfItemsToDelete.plus(task)
                                                }
                                            }
                                        ),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = cardColor,
                                        contentColor = Color.White
                                    ),
                                    border = BorderStroke(2.dp, borderColor)
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
                                                        uncheckedColor = Color.White.copy(0.6f)
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
                                                    unselectedColor = Color.White.copy(0.6f)
                                                )
                                            )
                                        }
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 8.dp)
                                                .animateContentSize(
                                                    animationSpec = tween(
                                                        durationMillis = 500
                                                    )
                                                )
                                        ) {
                                            AnimatedContent(
                                                textSize,
                                                transitionSpec = {
                                                    ContentTransform(
                                                        targetContentEnter =
                                                            fadeIn(
                                                                tween(
                                                                    800,
                                                                    easing = LinearOutSlowInEasing
                                                                )
                                                            ),
                                                        initialContentExit = fadeOut(
                                                            tween(
                                                                800,
                                                                easing = LinearOutSlowInEasing
                                                            )
                                                        )
                                                    )
                                                }) { size ->
                                                Text(
                                                    task.description,
                                                    modifier = Modifier,
                                                    fontFamily = FontFamily(Font(R.font.unbounded_medium)),
                                                    color = if (task.isCompleted == true)
                                                        Color.White.copy(0.8f)
                                                    else
                                                        Color.White,
                                                    fontSize = size,
                                                    textDecoration = if (task.isCompleted == true) TextDecoration.LineThrough else TextDecoration.None,
                                                    softWrap = true,
                                                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
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
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                NotoBottomBar(
                    navigationController = navigationController,
                    enableCheckboxToDelete = enableCheckboxToDelete,
                    listOfItemsToDelete = listOfItemsToDelete,
                    listOfNotesToDelete = null,
                    todoViewModel = todoViewModel,
                    noteViewModel = null,
                    showSearchBar = { updatedValue -> showSearchBar = updatedValue },
                    modifier = Modifier.offset {
                        IntOffset(
                            x = 0,
                            y = animatedYOffset.roundToInt()
                        )
                    },
                    openEditor = { firstPair ->
                        openEditorSheet = firstPair.first
                        taskToEdit = firstPair.second.first
                    }
                )
            }
        }
    }
}