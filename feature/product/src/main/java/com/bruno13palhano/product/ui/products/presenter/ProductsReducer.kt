package com.bruno13palhano.product.ui.products.presenter

import com.bruno13palhano.ui.shared.Reducer

internal class ProductsReducer : Reducer<ProductsState, ProductsEvent, ProductsEffect> {
    override fun reduce(
        previousState: ProductsState,
        event: ProductsEvent
    ): Pair<ProductsState, ProductsEffect?> {
        return when (event) {
            is ProductsEvent.EditProduct -> {
                previousState to ProductsEffect.NavigateToEditProduct(event.id)
            }

            is ProductsEvent.AddProduct -> {
                previousState to ProductsEffect.NavigateToAddProduct
            }

            is ProductsEvent.UpdateProducts -> {
                previousState.copy(products = event.products) to null
            }
        }
    }
}