package com.bruno13palhano.product.ui.screen

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
import com.bruno13palhano.product.R
import com.bruno13palhano.product.ui.shared.ProductsEffect
import com.bruno13palhano.product.ui.viewmodel.ProductsViewModel
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.components.ElevatedListItem
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun ProductsRoute(
    modifier: Modifier = Modifier,
    navigateToEditProduct: (id: Long) -> Unit,
    navigateToAddProduct: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getProducts() }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effect)

    val deleteMessage = stringResource(id = R.string.product_deleted)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(effect) {
        effect.collect { action ->
            when(action) {
                is ProductsEffect.NavigateToEditProduct -> {
                    navigateToEditProduct(action.id)
                }
                is ProductsEffect.NavigateToAddProduct -> {
                    navigateToAddProduct()
                }
                is ProductsEffect.DeleteProduct -> {
                    viewModel.deleteProduct(id = action.id)
                }
                is ProductsEffect.ShowDeletedMessage -> {
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

    ProductsContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        products = state.products,
        onEditProductItemClick = viewModel::updatingProductState,
        onDeleteItemClick = viewModel::onDeletingProductClick,
        onAddNewProductClick = viewModel::addButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    products: List<CommonItem>,
    onEditProductItemClick: (id: Long) -> Unit,
    onDeleteItemClick: (id: Long) -> Unit,
    onAddNewProductClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .semantics { contentDescription = "Products content" }
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.products)) }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics { contentDescription = "Add button" },
                onClick = onAddNewProductClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_product)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .semantics { contentDescription = "List of products" }
                .consumeWindowInsets(it),
            contentPadding = it
        ) {
            items(items = products, key = { product -> product.id }) { product ->
                ElevatedListItem(
                    modifier = Modifier.padding(4.dp),
                    icon = Icons.Filled.Delete,
                    iconDescription = stringResource(id = R.string.delete),
                    onItemClick = { onEditProductItemClick(product.id) },
                    onIconClick = { onDeleteItemClick(product.id) }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = product.title
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductsContentPreview() {
    ProductsContent(
        snackbarHostState = remember { SnackbarHostState() },
        products = listOf(
            CommonItem(id = 1, title = "product 1"),
            CommonItem(id = 2, title = "product 2"),
            CommonItem(id = 3, title = "product 3"),
            CommonItem(id = 4, title = "product 4"),
            CommonItem(id = 5, title = "product 5"),
            CommonItem(id = 6, title = "product 6"),
        ),
        onEditProductItemClick = {},
        onDeleteItemClick = {},
        onAddNewProductClick = {}
    )
}