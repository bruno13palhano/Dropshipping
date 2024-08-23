package com.bruno13palhano.ui.components

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun ElevatedListItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconDescription: String,
    shape: Shape = CardDefaults.elevatedShape,
    onItemClick: () -> Unit,
    onIconClick: () -> Unit,
    headlineContent: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = shape,
        onClick = onItemClick
    ) {
        ListItem(
            trailingContent = {
                IconButton(
                    modifier = Modifier.semantics { contentDescription = "Action icon" },
                    onClick = onIconClick
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = iconDescription
                    )
                }
            },
            headlineContent = headlineContent
        )
    }
}