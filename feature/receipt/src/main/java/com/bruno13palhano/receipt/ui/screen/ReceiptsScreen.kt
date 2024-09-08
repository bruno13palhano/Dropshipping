package com.bruno13palhano.receipt.ui.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.receipt.R
import com.bruno13palhano.receipt.ui.shared.ReceiptsEffect
import com.bruno13palhano.receipt.ui.viewmodel.ReceiptsViewModel
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.components.ElevatedListItem
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun ReceiptsRoute(
    modifier: Modifier = Modifier,
    onItemClick: (id: Long) -> Unit,
    onAddNewReceiptClick : () -> Unit,
    viewModel: ReceiptsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getReceipts() }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effect)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val deleteMessage = stringResource(id = R.string.delete_receipt_message)

    LaunchedEffect(effect) {
        effect.collect { action ->
            when (action) {
                is ReceiptsEffect.NavigateToEditReceipt -> {
                    onItemClick(action.id)
                }
                is ReceiptsEffect.NavigateToSearchProduct -> {
                    onAddNewReceiptClick()
                }
                is ReceiptsEffect.DeleteReceipt -> {
                    viewModel.deleteReceipt(id = action.id)
                }
                is ReceiptsEffect.ShowDeletedMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = deleteMessage,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }

    ReceiptsContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        receipts = state.receipts,
        onReceiptItemClick = viewModel::onEditReceiptClick,
        onDeleteReceiptClick = viewModel::onDeleteReceiptClick,
        onAddNewReceiptClick = viewModel::onAddReceiptClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptsContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    receipts: List<CommonItem>,
    onReceiptItemClick: (id: Long) -> Unit,
    onDeleteReceiptClick: (id: Long) -> Unit,
    onAddNewReceiptClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .semantics { contentDescription = "Receipts content" },
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.receipts)) }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewReceiptClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_receipt)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .semantics { contentDescription = "List of receipts" }
                .consumeWindowInsets(it),
            contentPadding = it
        ) {
            items(items = receipts, key = { receipt -> receipt.id }) { receipt ->
                ElevatedListItem(
                    modifier = Modifier.padding(4.dp),
                    icon = Icons.Filled.Delete,
                    iconDescription = stringResource(id = R.string.delete),
                    onItemClick = { onReceiptItemClick(receipt.id) },
                    onIconClick = { onDeleteReceiptClick(receipt.id) }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = receipt.title
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ReceiptsContentPreview() {
    ReceiptsContent(
        snackbarHostState = remember { SnackbarHostState() },
        receipts = listOf(
            CommonItem(id = 1, title = "receipt 1"),
            CommonItem(id = 2, title = "receipt 2"),
            CommonItem(id = 3, title = "receipt 3"),
            CommonItem(id = 4, title = "receipt 4"),
            CommonItem(id = 5, title = "receipt 5"),
            CommonItem(id = 6, title = "receipt 6"),
        ),
        onReceiptItemClick = {},
        onDeleteReceiptClick = {},
        onAddNewReceiptClick = {}
    )
}