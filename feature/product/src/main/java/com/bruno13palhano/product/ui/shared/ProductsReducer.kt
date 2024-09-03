package com.bruno13palhano.product.ui.shared

import com.bruno13palhano.ui.shared.Reducer

internal class ProductsReducer : Reducer<ProductsState, ProductsEvent, ProductsEffect> {
    override fun reduce(
        previousState: ProductsState,
        event: ProductsEvent
    ): Pair<ProductsState, ProductsEffect?> {
        return when (event) {
            is ProductsEvent.ProductUpdating -> {
                previousState.copy(
                    isIdle = false,
                    isProductUpdating = event.isUpdating,
                    isProductAdding = false,
                    isProductCanceling = false,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    isError = false,
                    currentProduct = event.product,
                ) to null
            }
            is ProductsEvent.ProductAdding -> {
                previousState.copy(
                    isIdle = false,
                    isProductUpdating = false,
                    isProductAdding = event.isAdding,
                    isProductCanceling = false,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    isError = false
                ) to null
            }
            is ProductsEvent.ProductCanceling -> {
                previousState.copy(
                    isIdle = false,
                    isProductUpdating = false,
                    isProductAdding = false,
                    isProductCanceling = event.isCanceling,
                    isProductDeleting = false,
                    isError = false
                ) to null
            }
            is ProductsEvent.ProductDeleting -> {
                previousState.copy(
                    isIdle = false,
                    isProductUpdating = false,
                    isProductAdding = false,
                    isProductCanceling = false,
                    isProductDeleting = event.isDeleting,
                    isError = false
                ) to ProductsEffect.ShowDeletedMessage
            }
            is ProductsEvent.IdleState -> {
                previousState.copy(
                    isProductsUpdating = false,
                    isProductUpdating = false,
                    isProductAdding = false,
                    isProductCanceling = false,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    isIdle = event.isIdle,
                    isError = false
                ) to null
            }
            is ProductsEvent.ProductUpdatingInvalidField -> {
                previousState.copy(
                    isIdle = false,
                    isProductUpdating = true,
                    isProductAdding = false,
                    isProductCanceling = false,
                    isProductDeleting = false,
                    hasInvalidField = event.hasInvalidField,
                    isError = false
                ) to ProductsEffect.ShowErrorMessage
            }
            is ProductsEvent.ProductAddingInvalidField -> {
                previousState.copy(
                    isIdle = false,
                    isProductUpdating = false,
                    isProductAdding = true,
                    isProductCanceling = false,
                    isProductDeleting = false,
                    hasInvalidField = event.hasInvalidField,
                    isError = false
                ) to ProductsEffect.ShowErrorMessage
            }
            is ProductsEvent.ErrorState -> {
                previousState.copy(
                    isIdle = false,
                    isProductUpdating = false,
                    isProductAdding = false,
                    isProductCanceling = false,
                    isProductDeleting = false,
                    isError = event.isError
                ) to ProductsEffect.ShowErrorMessage
            }
            is ProductsEvent.UpdateProducts -> {
                previousState.copy(
                    isProductsUpdating = event.isUpdating,
                    products = event.products
                ) to null
            }
        }
    }
}