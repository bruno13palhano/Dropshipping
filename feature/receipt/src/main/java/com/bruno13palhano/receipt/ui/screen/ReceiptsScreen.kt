package com.bruno13palhano.receipt.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.receipt.R
import com.bruno13palhano.receipt.ui.shared.ReceiptsEffect
import com.bruno13palhano.receipt.ui.shared.ReceiptsEvent
import com.bruno13palhano.receipt.ui.viewmodel.ReceiptsViewModel
import com.bruno13palhano.ui.components.dateFormat
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun ReceiptsRoute(
    modifier: Modifier = Modifier,
    onItemClick: (id: Long) -> Unit,
    onAddNewReceiptClick : () -> Unit,
    viewModel: ReceiptsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getReceipts() }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val receipts = state.receipts.collectAsLazyPagingItems()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effect)

    LaunchedEffect(effect) {
        effect.collect { action ->
            when (action) {
                is ReceiptsEffect.NavigateToEditReceipt -> {
                    onItemClick(action.id)
                }

                is ReceiptsEffect.NavigateToSearchProduct -> {
                    onAddNewReceiptClick()
                }
            }
        }
    }

    ReceiptsContent(
        modifier = modifier,
        receipts = receipts,
        onReceiptItemClick = { id ->
            viewModel.sendEvent(
                event = ReceiptsEvent.EditReceipt(editReceipt = true, id = id)
            )
        },
        onAddNewReceiptClick = {
            viewModel.sendEvent(event = ReceiptsEvent.SearchProduct(searching = true))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptsContent(
    modifier: Modifier = Modifier,
    receipts: LazyPagingItems<Receipt>,
    onReceiptItemClick: (id: Long) -> Unit,
    onAddNewReceiptClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .semantics { contentDescription = "Receipts content" },
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.receipts)) }) },
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
            contentPadding = PaddingValues(
                vertical = 4.dp + it.calculateTopPadding(),
                horizontal = 4.dp + it.calculateStartPadding(LayoutDirection.Ltr),
            )
        ) {
            items(count = receipts.itemCount) { index ->
                receipts[index]?.let { receipt ->
                    ElevatedCard(
                        modifier = Modifier.padding(4.dp),
                        onClick = { onReceiptItemClick(receipt.id) }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 4.dp)
                                .fillMaxWidth(),
                            text = receipt.customerName
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            text = receipt.product.name
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            text = stringResource(
                                id = R.string.units_sold_info,
                                receipt.quantity
                            )
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 8.dp)
                                .fillMaxWidth(),
                            text = stringResource(
                                id = R.string.data_info,
                                dateFormat.format(receipt.requestDate)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ReceiptsContentPreview() {
    ReceiptsContent(
        receipts = flowOf(
            PagingData.from(
                listOf(
                    Receipt(
                        id = 1L,
                        product = Product(1L, "12","Homem"),
                        requestNumber = 1,
                        requestDate = 1719999234567L,
                        customerName = "First Customer",
                        quantity = 1,
                        naturaPrice = 56.99f,
                        amazonPrice = 88.9f,
                        paymentOption = "cash",
                        canceled = false,
                        observations = ""
                    ),
                    Receipt(
                        id = 2L,
                        product = Product(2L, "12","Essencial"),
                        requestNumber = 1,
                        requestDate = 1719999234567L,
                        customerName = "Second Customer",
                        quantity = 2,
                        naturaPrice = 56.99f,
                        amazonPrice = 88.9f,
                        paymentOption = "cash",
                        canceled = false,
                        observations = ""
                    )
                )
            )
        ).collectAsLazyPagingItems(),
        onReceiptItemClick = {},
        onAddNewReceiptClick = {}
    )
}