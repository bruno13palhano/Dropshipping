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
                    products = event.products
                ) to null
            }
            is ProductsEvent.EditProduct -> {
                previousState.copy(
                    productsLoading = false,
                    editProduct = event.editProduct,
                    addProduct = false,
                    deleteProduct = false
                ) to ProductsEffect.NavigateToEditProduct(event.id)
            }
            is ProductsEvent.AddProduct -> {
                previousState.copy(
                    productsLoading = false,
                    editProduct = false,
                    addProduct = event.addProduct,
                    deleteProduct = false
                ) to ProductsEffect.NavigateToAddProduct
            }
            is ProductsEvent.UpdateDeletingProduct -> {
                previousState.copy(
                    productsLoading = false,
                    editProduct = false,
                    addProduct = false,
                    deleteProduct = event.isDeleting
                ) to ProductsEffect.DeleteProduct(id = event.id)
            }
            is ProductsEvent.OnDeleteProductSuccessfully -> {
                previousState.copy(deleteProduct = false) to ProductsEffect.ShowDeletedMessage
            }
        }
    }
}