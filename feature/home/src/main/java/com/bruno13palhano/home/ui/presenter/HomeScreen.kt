package com.bruno13palhano.home.ui.presenter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.home.R
import com.bruno13palhano.home.ui.viewmodel.HomeViewModel
import com.bruno13palhano.model.MostSaleItem
import com.bruno13palhano.model.Profit
import com.bruno13palhano.model.ReceiptItem
import com.bruno13palhano.ui.components.ExpandedListItem
import com.bruno13palhano.ui.components.dateFormat

@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    HomeContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onAction: (action: HomeAction) -> Unit
) {
    Scaffold(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .verticalScroll(rememberScrollState())
        ) {
            ProfitInfo(
                profitAmount = state.profit.profit,
                profitVisibility = state.profitVisible,
                onToggleProfitVisibility = { visible ->
                    onAction(HomeAction.OnProfitVisibilityChanged(visible = visible))
                }
            )

            OutlinedCard(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                ElevatedCard(modifier = Modifier.padding(8.dp)) {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 4.dp)
                            .fillMaxWidth(),
                        text = stringResource(
                            id = R.string.amazon_profit, state.profit.amazonProfit
                        ),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                        text = stringResource(
                            id = R.string.natura_profit, state.profit.naturaProfit
                        ),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }

            if (state.receiptsVisible) {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.last_receipts),
                    style = MaterialTheme.typography.bodyLarge,
                )

                OutlinedCard(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        state.lastReceipts.forEach { receipt ->
                            ExpandedListItem(
                                modifier = Modifier.padding(vertical = 1.dp),
                                title = stringResource(
                                    id = R.string.title_info,
                                    dateFormat.format(receipt.requestDate),
                                    receipt.customerName
                                ),
                                expanded = state.expandedItems.find { item ->
                                    item.first == receipt.id
                                }?.second ?: false,
                                shape = RectangleShape,
                                onClick = { expanded ->
                                    onAction(
                                        HomeAction.OnExpandedItemChanged(
                                            id = receipt.id,
                                            expanded = expanded
                                        )
                                    )
                                }
                            ) {
                                Text(
                                    text = stringResource(
                                        id = R.string.product_info,
                                        receipt.productName
                                    ),
                                    fontStyle = FontStyle.Italic,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.amazon_price_info,
                                        receipt.amazonPrice
                                    ),
                                    fontStyle = FontStyle.Italic,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.most_sale_info),
                    style = MaterialTheme.typography.bodyLarge,
                )

                OutlinedCard(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        state.mostSale.forEach {
                            Card(
                                modifier = Modifier.padding(vertical = 1.dp),
                                shape = RectangleShape
                            ) {
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = stringResource(
                                                id = R.string.most_sale_title_info,
                                                it.productName,
                                                it.unitsSold
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfitInfo(
    profitAmount: Float,
    profitVisibility: Boolean,
    onToggleProfitVisibility: (showProfit: Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .weight(1f),
            text =
            if (profitVisibility) {
                stringResource(id = R.string.profit_amount_info, profitAmount)
            } else {
                stringResource(id = R.string.dots)
            },
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(
            modifier = Modifier.padding(end = 8.dp),
            onClick = { onToggleProfitVisibility(!profitVisibility) }
        ) {
            if (profitVisibility) {
                Icon(
                    imageVector = Icons.Filled.Visibility,
                    contentDescription = stringResource(id = R.string.visible)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.VisibilityOff,
                    contentDescription = stringResource(id = R.string.not_visible)
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomePreview() {
    HomeContent(
        state = HomeState(
            profitVisible = true,
            receiptsVisible = true,
            profit = Profit(0f, 0f, 0f),
            lastReceipts = listOf(
                ReceiptItem(
                    id = 1,
                    customerName = "customer 1",
                    productName = "product 1",
                    amazonPrice = 1f,
                    requestDate = 1L
                ),
                ReceiptItem(
                    id = 2,
                    customerName = "customer 2",
                    productName = "product 2",
                    amazonPrice = 2f,
                    requestDate = 2L
                ),
                ReceiptItem(
                    id = 3,
                    customerName = "customer 3",
                    productName = "product 3",
                    amazonPrice = 3f,
                    requestDate = 3L
                )
            ),
            mostSale = listOf(
                MostSaleItem(
                    id = 1,
                    productName = "product 1",
                    unitsSold = 1
                ),
                MostSaleItem(
                    id = 2,
                    productName = "product 2",
                    unitsSold = 2
                ),
                MostSaleItem(
                    id = 3,
                    productName = "product 3",
                    unitsSold = 3
                )
            ),
            expandedItems = emptyList()
        ),
        onAction = {}
    )
}