package com.bruno13palhano.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun ElevatedListItem(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit,
    headlineContent: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onItemClick
    ) {
        ListItem(
            trailingContent = {
                IconButton(
                    modifier = Modifier.semantics { contentDescription = "Delete icon" },
                    onClick = onDeleteItemClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                }
            },
            headlineContent = headlineContent
        )
    }
}