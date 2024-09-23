package com.bruno13palhano.product.ui.shared

import com.bruno13palhano.ui.shared.Reducer

internal class ProductsReducer : Reducer<ProductsState, ProductsEvent, ProductsEffect> {
    override fun reduce(
        previousState: ProductsState,
        event: ProductsEvent
    ): Pair<ProductsState, ProductsEffect?> {
        return when (event) {
            is ProductsEvent.EditProduct -> {
                previousState.copy(
                    editProduct = event.editProduct,
                    addProduct = false
                ) to ProductsEffect.NavigateToEditProduct(event.id)
            }
            is ProductsEvent.AddProduct -> {
                previousState.copy(
                    editProduct = false,
                    addProduct = event.addProduct
                ) to ProductsEffect.NavigateToAddProduct
            }
        }
    }
}