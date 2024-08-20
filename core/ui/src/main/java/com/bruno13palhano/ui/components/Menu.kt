package com.bruno13palhano.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MoreVertMenu(
    items: List<String>,
    expanded: Boolean,
    onDismissRequest: (expanded: Boolean) -> Unit,
    onItemClick: (index: Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest(false) }
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(
                text = { Text(text = item) },
                onClick = {
                    onItemClick(index)
                    onDismissRequest(false)
                }
            )
        }
    }
}