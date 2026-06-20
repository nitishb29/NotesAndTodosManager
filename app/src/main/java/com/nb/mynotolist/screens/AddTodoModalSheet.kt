package com.nb.mynotolist.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nb.mynotolist.R
import com.nb.mynotolist.db.entities.TodoEntity
import com.nb.mynotolist.ui.theme.cardColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoModalSheet(
    todoItem: TodoEntity?,
    onCancel: () -> Unit,
    onSave: (String) -> Unit
) {
    var taskDescription by remember { mutableStateOf(todoItem?.description ?: "") }
    ModalBottomSheet(
        onDismissRequest = onCancel,
        modifier = Modifier.padding(5.dp),
        containerColor = cardColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                placeholder = {
                    Text(
                        text = if (todoItem == null) "Add Todo Item Here" else "",
                        fontFamily = FontFamily(Font(R.font.unbounded_semibold))
                    )
                },
                modifier = Modifier.fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 10.dp, vertical = 15.dp)
                ,
                singleLine = false,
                maxLines = 10,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.DarkGray,
                    unfocusedPlaceholderColor = Color.White.copy(0.5f),
                    focusedPlaceholderColor = Color.White,
                    focusedIndicatorColor = Color.White.copy(0.3f)
                )
            )
            IconButton(
                onClick = { onSave(taskDescription.trim()) },
                enabled = taskDescription.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 170.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.save_icon),
                        contentDescription = null,
                        modifier = Modifier.scale(1.4f)
                    )
                    Text(
                        "Save",
                        fontFamily = FontFamily(Font(R.font.unbounded_semibold)),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}