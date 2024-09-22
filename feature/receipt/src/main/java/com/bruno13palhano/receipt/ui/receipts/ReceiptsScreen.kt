package com.bruno13palhano.receipt.ui.receipts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.model.Product
import com.bruno13palhano.model.Receipt
import com.bruno13palhano.receipt.R
import com.bruno13palhano.ui.components.dateFormat
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle

@Composable
internal fun ReceiptsRoute(
    modifier: Modifier = Modifier,
    onItemClick: (id: Long) -> Unit,
    onAddNewReceiptClick : () -> Unit,
    viewModel: ReceiptsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val effects = rememberFlowWithLifecycle(flow = viewModel.effects)

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is ReceiptsEffect.NavigateToEditReceipt -> onItemClick(effect.id)

                is ReceiptsEffect.NavigateToSearchProduct -> onAddNewReceiptClick()
            }
        }
    }

    ReceiptsContent(
        modifier = modifier,
        receipts = state.receipts,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptsContent(
    modifier: Modifier = Modifier,
    receipts: List<Receipt>,
    onAction: (action: ReceiptsAction) -> Unit
) {
    Scaffold(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .semantics { contentDescription = "Receipts content" },
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.receipts)) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(ReceiptsAction.OnSearchProductClick(searching = true))
                }
            ) {
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
            items(items = receipts, key = { receipt -> receipt.id}) { receipt ->
                ElevatedCard(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        onAction(
                            ReceiptsAction.OnEditReceiptClick(
                                editReceipt = true,
                                id = receipt.id
                            )
                        )
                    }
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

@Preview(showSystemUi = true)
@Composable
private fun ReceiptsContentPreview() {
    ReceiptsContent(
        receipts =
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
            ),
        onAction = {}
    )
}