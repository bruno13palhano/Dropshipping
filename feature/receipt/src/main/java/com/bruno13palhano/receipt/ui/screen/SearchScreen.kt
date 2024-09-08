package com.bruno13palhano.receipt.ui.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.receipt.R
import com.bruno13palhano.receipt.ui.shared.SearchEffect
import com.bruno13palhano.receipt.ui.viewmodel.SearchViewModel
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.components.ElevatedListItem
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle

@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    navigateToAddReceipt: (productId: Long) -> Unit,
    navigateBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effect)

    LaunchedEffect(effect) {
        effect.collect { action ->
            when (action) {
                is SearchEffect.NavigateToAddReceipt -> {
                    navigateToAddReceipt(action.productId)
                }
                is SearchEffect.NavigateBack -> {
                    navigateBack()
                }
                is SearchEffect.SearchingProducts -> {
                    viewModel.searchProducts(query = action.query)
                }
                is SearchEffect.DeleteCache -> {
                    viewModel.deleteCache(query = action.query)
                }
                is SearchEffect.RefreshProducts -> {
                    viewModel.getProducts()
                }
            }
        }
    }

    SearchContent(
        modifier = modifier,
        query = state.query,
        active = state.active,
        products = state.products,
        cache = state.cache,
        onQueryChange = viewModel::onQueryChange,
        onActiveChange = viewModel::onActiveChange,
        onSearchClick = viewModel::onSearchClick,
        onProductItemClick = viewModel::navigateToAddReceipt,
        onDeleteSearchClick = viewModel::onDeleteSearchClick,
        onClose = viewModel::onCloseSearchClick
    )
}

@Composable
private fun SearchContent(
    modifier: Modifier = Modifier,
    query: String,
    active: Boolean,
    products: List<CommonItem>,
    cache: List<String>,
    onQueryChange: (query: String) -> Unit,
    onActiveChange: (active: Boolean) -> Unit,
    onSearchClick: (query: String) -> Unit,
    onProductItemClick: (id: Long) -> Unit,
    onDeleteSearchClick: (query: String) -> Unit,
    onClose: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .semantics { contentDescription = "Search content" },
        topBar = {
            SearchProducts(
                query = query,
                active = active,
                cache = cache,
                onQueryChange = onQueryChange,
                onActiveChange = onActiveChange,
                onSearchClick = onSearchClick,
                onDeleteSearchClick = onDeleteSearchClick,
                onClose = onClose
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .semantics { contentDescription = "List of products" }
                .consumeWindowInsets(it),
            contentPadding = it
        ) {
            items(items = products, key = { product -> product.id }) {
                ElevatedListItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    iconDescription = "",
                    onItemClick = { onProductItemClick(it.id) },
                    onIconClick = { onProductItemClick(it.id) }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.title
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchProducts(
    modifier: Modifier = Modifier,
    query: String,
    active: Boolean,
    cache: List<String>,
    onQueryChange: (query: String) -> Unit,
    onActiveChange: (active: Boolean) -> Unit,
    onSearchClick: (query: String) -> Unit,
    onDeleteSearchClick: (query: String) -> Unit,
    onClose: () -> Unit
) {
    SearchBar(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            onActiveChange(false)
            onSearchClick(it)
        },
        active = active,
        onActiveChange = onActiveChange,
        leadingIcon = {
            IconButton(
                modifier = Modifier.semantics { contentDescription = "Close button" },
                onClick = { if (active) onActiveChange(false) else onClose()
                }
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
                    onActiveChange(false)
                    onSearchClick(query)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search_products)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.semantics { contentDescription = "List of queries" },
            reverseLayout = true
        ) {
            items(items = cache, key = { it }) {
                ElevatedListItem(
                    icon = Icons.Filled.Close,
                    iconDescription = stringResource(id = R.string.delete),
                    shape = RectangleShape,
                    onItemClick = {
                        onActiveChange(false)
                        onSearchClick(it)
                    },
                    onIconClick = { onDeleteSearchClick(it) }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    SearchContent(
        query = "product 1",
        active = false,
        products = listOf(
            CommonItem(id = 1, title = "product 1"),
            CommonItem(id = 2, title = "product 2"),
            CommonItem(id = 3, title = "product 3")
        ),
        cache = listOf(
            "search 1",
            "search 2",
            "search 3"
        ),
        onQueryChange = {},
        onActiveChange = {},
        onSearchClick = {},
        onProductItemClick = {},
        onDeleteSearchClick = {},
        onClose = {}
    )
}