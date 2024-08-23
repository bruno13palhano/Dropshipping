package com.bruno13palhano.receipt.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.receipt.R
import com.bruno13palhano.receipt.ui.viewmodel.ReceiptsViewModel
import com.bruno13palhano.ui.clickableWithoutRipple
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.components.ElevatedListItem

@Composable
internal fun ReceiptsRoute(
    modifier: Modifier = Modifier,
    onItemClick: (id: Long) -> Unit,
    onAddNewReceiptClick : (productId: Long) -> Unit,
    viewModel: ReceiptsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getReceipts() }
    LaunchedEffect(key1 = Unit) { viewModel.getProducts() }

    val receipts by viewModel.receipts.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()

    var showProductsSearch by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ReceiptsContent(
        modifier = modifier
            .clickableWithoutRipple {
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
                showProductsSearch = false
            },
        snackbarHostState = snackbarHostState,
        showProductsSearch = showProductsSearch,
        products = products,
        receipts = receipts,
        onReceiptItemClick = onItemClick,
        onDeleteReceiptClick = viewModel::deleteReceipt,
        onAddNewReceiptClick = {
            showProductsSearch = true
            viewModel.getProducts()
        },
        onSearchClick =  {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
            viewModel.searchProducts(it)
        },
        onProductItemClick = {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
            showProductsSearch = true
            onAddNewReceiptClick(it)
        },
        onClose = { showProductsSearch = false }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptsContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    showProductsSearch: Boolean,
    receipts: List<CommonItem>,
    products: List<CommonItem>,
    onReceiptItemClick: (id: Long) -> Unit,
    onDeleteReceiptClick: (id: Long) -> Unit,
    onAddNewReceiptClick: () -> Unit,
    onSearchClick: (query: String) -> Unit,
    onProductItemClick: (id: Long) -> Unit,
    onClose: () -> Unit
) {
    Scaffold(
        modifier = modifier.semantics { contentDescription = "Receipts content" },
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
        AnimatedVisibility(
            visible = !showProductsSearch,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier
                    .semantics { contentDescription = "List of receipts" }
                    .padding(it),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(items = receipts, key = { receipt -> receipt.id }) { receipt ->
                    ElevatedListItem(
                        modifier = Modifier.padding(4.dp),
                        onItemClick = { onReceiptItemClick(receipt.id) },
                        onDeleteItemClick = { onDeleteReceiptClick(receipt.id) }
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
    AnimatedVisibility(
        visible = showProductsSearch,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BackHandler { onClose() }

        SearchProducts(
            modifier = Modifier,
            products = products,
            onSearchClicked = onSearchClick,
            onItemClicked = onProductItemClick,
            onClose = onClose
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchProducts(
    modifier: Modifier = Modifier,
    products: List<CommonItem>,
    onSearchClicked: (query: String) -> Unit,
    onItemClicked: (id: Long) -> Unit,
    onClose: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Column {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                active = false
                onSearchClicked(it)
            },
            placeholder = { Text(text = stringResource(id = R.string.search_products)) },
            active = active,
            onActiveChange = { active = it },
            leadingIcon = {
                IconButton(
                    modifier = Modifier.semantics { contentDescription = "Close button" },
                    onClick = { if (active) active = false else onClose() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    modifier = Modifier.semantics { contentDescription = "Search button" },
                    onClick = {
                        active = false
                        onSearchClicked(query)
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                }
            }
        ) {
            // TODO: implement search cache
        }

        LazyColumn(
            modifier = modifier.semantics { contentDescription = "List of products" },
            contentPadding = PaddingValues(4.dp)
        ) {
            items(items = products, key = { product -> product.id }) {
                ElevatedCard(
                    modifier = Modifier.padding(4.dp),
                    onClick = { onItemClicked(it.id) }
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.title
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReceiptsContentPreview() {
    ReceiptsContent(
        snackbarHostState = remember { SnackbarHostState() },
        showProductsSearch = false,
        receipts = listOf(
            CommonItem(id = 1, title = "receipt 1"),
            CommonItem(id = 2, title = "receipt 2"),
            CommonItem(id = 3, title = "receipt 3"),
            CommonItem(id = 4, title = "receipt 4"),
            CommonItem(id = 5, title = "receipt 5"),
            CommonItem(id = 6, title = "receipt 6"),
        ),
        products = listOf(
            CommonItem(id = 1, title = "product 1"),
            CommonItem(id = 2, title = "product 2"),
            CommonItem(id = 3, title = "product 3")
        ),
        onReceiptItemClick = {},
        onDeleteReceiptClick = {},
        onAddNewReceiptClick = {},
        onSearchClick = {},
        onProductItemClick = {},
        onClose = {}
    )
}

@Preview
@Composable
private fun SearchProductsPreview() {
    SearchProducts(
        products = listOf(
            CommonItem(id = 1, title = "product 1"),
            CommonItem(id = 2, title = "product 2"),
            CommonItem(id = 3, title = "product 3"),
            CommonItem(id = 4, title = "product 4"),
            CommonItem(id = 5, title = "product 5"),
            CommonItem(id = 6, title = "product 6"),
        ),
        onSearchClicked = {},
        onItemClicked = {},
        onClose = {}
    )
}