package com.example.todolist.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.domain.Todo
import com.example.todolist.domain.todo1
import com.example.todolist.domain.todo2
import com.example.todolist.ui.theme.ToDoListTheme

@Composable
fun TodoItem(
    todo: Todo,
    onCompleteChange: (Boolean) -> Unit,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textAlpha = if (todo.isCompleted) 0.5f else 1f
    val textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
    val cardColor = if (todo.isCompleted)
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    else MaterialTheme.colorScheme.surface

    Card(
        onClick = onItemClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = onCompleteChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = textAlpha),
                    textDecoration = textDecoration
                )

                todo.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = textAlpha),
                        textDecoration = textDecoration,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f) // Vermelho sutil
                )
            }
        }
    }

    /*Surface(
        onClick = onItemClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,

        ){
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = onCompleteChange,
            )

            //Só funciona dentro do row
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier
                .weight(1f))
            {
                Text(text = todo.tittle,
                    style = MaterialTheme.typography.titleLarge
                )

                //Faz os espaços entre os textos apenas se "description" não for nulo
                todo.description?.let {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = todo.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }

            IconButton(
                onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }

    }*/
}


@Preview
@Composable
private fun TodoItemPreview() {
    ToDoListTheme {
        TodoItem(
            todo = todo1,
            onCompleteChange = {},
            onItemClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview
@Composable
private fun TodoItemCompletedePreview() {
    ToDoListTheme {
        TodoItem(
            todo = todo2,
            onCompleteChange = {},
            onItemClick = {},
            onDeleteClick = {},
        )
    }
}