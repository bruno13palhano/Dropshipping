package com.bruno13palhano.product.ui.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.components.CommonItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
internal fun productsPresenter(
    products: Flow<List<Product>>,
    events: Flow<ProductsEvent>,
    sendEffect: (ProductsEffect) -> Unit
): ProductsState {
    val productList by products
        .map {
            it.map { product ->
                CommonItem(
                    id = product.id,
                    title = product.name
                )
            }
        }
        .collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ProductsEvent.AddProduct -> sendEffect(ProductsEffect.NavigateToAddProduct)

                is ProductsEvent.EditProduct -> {
                    sendEffect(ProductsEffect.NavigateToEditProduct(id = event.id))
                }
            }
        }
    }

    return ProductsState(products = productList)
}