package com.bruno13palhano.product.ui.shared

import com.bruno13palhano.ui.shared.Reducer

internal class ProductsReducer : Reducer<ProductsState, ProductsEvent, ProductsEffect> {
    override fun reduce(
        previousState: ProductsState,
        event: ProductsEvent
    ): Pair<ProductsState, ProductsEffect?> {
        return when (event) {
            is ProductsEvent.UpdateProducts -> {
                previousState.copy(
                    productsLoading = event.isLoading,
                    editProduct = false,
                    addProduct = false,
                    deleteProduct = false,
                    isIdle = false,
                    isError = false,
                    products = event.products
                ) to null
            }
            is ProductsEvent.EditProduct -> {
                previousState.copy(
                    productsLoading = false,
                    editProduct = event.editProduct,
                    addProduct = false,
                    deleteProduct = false,
                    isIdle = false,
                    isError = false
                ) to ProductsEffect.NavigateToEditProduct(event.id)
            }
            is ProductsEvent.AddProduct -> {
                previousState.copy(
                    productsLoading = false,
                    editProduct = false,
                    addProduct = event.addProduct,
                    deleteProduct = false,
                    isIdle = false,
                    isError = false
                ) to ProductsEffect.NavigateToAddProduct
            }
            is ProductsEvent.ProductDeleting -> {
                previousState.copy(
                    productsLoading = false,
                    editProduct = false,
                    addProduct = false,
                    deleteProduct = event.isDeleting,
                    isIdle = false,
                    isError = false
                ) to ProductsEffect.ShowDeletedMessage
            }
            is ProductsEvent.IdleState -> {
                previousState.copy(
                    productsLoading = false,
                    editProduct = false,
                    addProduct = false,
                    deleteProduct = false,
                    isIdle = event.isIdle,
                    isError = false
                ) to null
            }
            is ProductsEvent.ErrorState -> {
                previousState.copy(
                    productsLoading = false,
                    editProduct = false,
                    addProduct = false,
                    deleteProduct = false,
                    isIdle = false,
                    isError = event.isError
                ) to ProductsEffect.ShowErrorMessage
            }
        }
    }
}