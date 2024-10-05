package com.bruno13palhano.product.ui.products.presenter

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
import com.bruno13palhano.product.R
import com.bruno13palhano.product.ui.products.viewmodel.ProductsViewModel
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle

@Composable
internal fun ProductsRoute(
    modifier: Modifier = Modifier,
    navigateToEditProduct: (id: Long) -> Unit,
    navigateToAddProduct: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effects)

    LaunchedEffect(effect) {
        effect.collect { action ->
            when(action) {
                is ProductsEffect.NavigateToEditProduct -> {
                    navigateToEditProduct(action.id)
                }
                is ProductsEffect.NavigateToAddProduct -> {
                    navigateToAddProduct()
                }
            }
        }
    }

    ProductsContent(
        modifier = modifier,
        products = state.products,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsContent(
    modifier: Modifier = Modifier,
    products: List<CommonItem>,
    onAction: (action: ProductsAction) -> Unit
) {
    Scaffold(
        modifier = modifier
            .semantics { contentDescription = "Products content" }
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.products)) }) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics { contentDescription = "Add product" },
                onClick = { onAction(ProductsAction.OnAddProductClick) }
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
            contentPadding = PaddingValues(
                vertical = 4.dp + it.calculateTopPadding(),
                horizontal = 4.dp + it.calculateStartPadding(LayoutDirection.Ltr),
            )
        ) {
            items(items = products, key = { product -> product.id }) { product ->
                ElevatedCard(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        onAction(
                            ProductsAction.OnEditProductClick(id = product.id)
                        )
                    }
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                            .fillMaxWidth(),
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
        products = listOf(
            CommonItem(id = 1, title = "product 1"),
            CommonItem(id = 2, title = "product 2"),
            CommonItem(id = 3, title = "product 3"),
            CommonItem(id = 4, title = "product 4"),
            CommonItem(id = 5, title = "product 5"),
            CommonItem(id = 6, title = "product 6"),
        ),
        onAction = {}
    )
}