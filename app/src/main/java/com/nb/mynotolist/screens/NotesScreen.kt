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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nb.mynotolist.R
import com.nb.mynotolist.db.entities.NoteEntity
import com.nb.mynotolist.ui.theme.bgColor
import com.nb.mynotolist.ui.theme.cardColor
import com.nb.mynotolist.viewmodel.NoteViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    navigationController: NavHostController,
    noteViewModel: NoteViewModel
) {
    val notes by noteViewModel.notesState.collectAsState()
    var enableCheckboxToDelete by remember { mutableStateOf(false) }
    var listOfNotesToDelete by remember { mutableStateOf(listOf<NoteEntity>()) }
    var openEditorSheet by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<NoteEntity?>(null) }
    var showSearchBar by remember { mutableStateOf(false) }
    val valueToSearch by noteViewModel.searchQuery.collectAsState()

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
                    isBottomBarVisible = false // Fold on scroll down
                } else if (available.y > 10) {
                    isBottomBarVisible = true  // Expand on scroll up
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
        )
        {
            Box(modifier = Modifier.align(Alignment.TopCenter)) {
                Column {
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
                                            noteViewModel.onSearchQueryChanged(newValue)
                                        },
                                        placeholder = {
                                            Text(
                                                text = "Note",
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
                                                onClick = { noteViewModel.onSearchQueryChanged("") },
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
                                        .padding(vertical = 23.11.dp, horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = "Notes",
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                                        color = Color.White
                                    )
                                }
                            }
                        }
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
                                val textSize = if (isExpanded) 18.sp else 15.sp
                                val infiniteTransition = rememberInfiniteTransition()
                                val selectedCardBorderColor by infiniteTransition.animateColor(
                                    initialValue = Color.Gray.copy(0.4f),
                                    targetValue = Color.White.copy(0.4f),
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(500, easing = FastOutSlowInEasing),
                                        repeatMode = RepeatMode.Reverse
                                    )
                                )
                                val borderColor =
                                    if (isTaskSelected) selectedCardBorderColor else Color.White.copy(
                                        0.2f
                                    )
                                Card(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(24.dp))
                                        .combinedClickable(
                                            onClick = { isExpanded = !isExpanded },
                                            onLongClick = {
                                                enableCheckboxToDelete =
                                                    enableCheckboxToDelete.not()
                                                isTaskSelected = isTaskSelected.not()
                                                if (isTaskSelected) {
                                                    listOfNotesToDelete =
                                                        listOfNotesToDelete.plus(note)
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
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(vertical = 12.5.dp),
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .animateContentSize(
                                                    animationSpec = tween(
                                                        durationMillis = 300
                                                    )
                                                ),
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
                                                        uncheckedColor = Color.White.copy(0.6f)
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
                                                    fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                                                    fontSize = 22.sp,
                                                )
                                                AnimatedContent(
                                                    textSize,
                                                    transitionSpec = {
                                                        ContentTransform(
                                                            targetContentEnter =
                                                                fadeIn(
                                                                    tween(
                                                                        500,
                                                                        easing = LinearOutSlowInEasing
                                                                    )
                                                                ),
                                                            initialContentExit = fadeOut(
                                                                tween(
                                                                    500,
                                                                    easing = LinearOutSlowInEasing
                                                                )
                                                            )
                                                        )
                                                    }) { size ->
                                                    Text(
                                                        note.description,
                                                        modifier = Modifier
                                                            .padding(horizontal = 25.dp)
                                                            .padding(bottom = 15.dp),
                                                        fontFamily = FontFamily(Font(R.font.unbounded_regular)),
                                                        color = if (isExpanded) Color.White else Color.LightGray,
                                                        softWrap = true,
                                                        overflow = TextOverflow.Ellipsis,
                                                        fontSize = size,
                                                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
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
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                NotoBottomBar(
                    navigationController,
                    enableCheckboxToDelete,
                    listOfItemsToDelete = null,
                    listOfNotesToDelete,
                    noteViewModel = noteViewModel,
                    todoViewModel = null,
                    showSearchBar = { updatedValue -> showSearchBar = updatedValue },
                    modifier = Modifier.offset {
                        IntOffset(
                            x = 0,
                            y = animatedYOffset.roundToInt()
                        )
                    },
                    openEditor = { firstPair ->
                        openEditorSheet = firstPair.first
                        noteToEdit = firstPair.second.second
                    }
                )
            }
        }
    }
}