package com.bruno13palhano.product.ui.shared

import com.bruno13palhano.ui.shared.Reducer

internal class ProductReducer : Reducer<ProductState, ProductEvent, ProductEffect> {
    override fun reduce(
        previousState: ProductState,
        event: ProductEvent
    ): Pair<ProductState, ProductEffect?> {
        return when (event) {
            is ProductEvent.UpdateLoadingCurrentProduct -> {
                previousState.copy(
                    loadingCurrentProduct = true,
                    isIdle = false,
                    editingProduct = false,
                    addingNewProduct = false,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    navigateBack = false
                ) to null
            }
            is ProductEvent.UpdateCurrentProduct -> {
                previousState.copy(
                    loadingCurrentProduct = true,
                    isIdle = false,
                    editingProduct = false,
                    addingNewProduct = false,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    navigateBack = false,
                    currentProduct = event.product
                ) to null
            }
            is ProductEvent.UpdateEditingProduct -> {
                previousState.copy(
                    loadingCurrentProduct = false,
                    isIdle = false,
                    editingProduct = true,
                    addingNewProduct = false,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    navigateBack = false
                ) to null
            }
            is ProductEvent.UpdateAddingNewProduct -> {
                previousState.copy(
                    loadingCurrentProduct = false,
                    isIdle = false,
                    editingProduct = false,
                    addingNewProduct = true,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    navigateBack = false
                ) to null
            }
            is ProductEvent.UpdateDeletingProduct -> {
                previousState.copy(
                    loadingCurrentProduct = false,
                    isIdle = false,
                    editingProduct = false,
                    addingNewProduct = false,
                    isProductDeleting = true,
                    hasInvalidField = false,
                    navigateBack = false
                ) to null
            }
            is ProductEvent.OnEditingProductDoneClick -> {
                previousState.copy(
                    loadingCurrentProduct = false,
                    isIdle = false,
                    editingProduct = true,
                    addingNewProduct = false,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    navigateBack = true
                ) to ProductEffect.NavigateBack
            }
            is ProductEvent.OnAddingNewProductDoneClick -> {
                previousState.copy(
                    loadingCurrentProduct = false,
                    isIdle = false,
                    editingProduct = false,
                    addingNewProduct = true,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    navigateBack = true
                ) to ProductEffect.NavigateBack
            }
            is ProductEvent.OnDeleteClick -> {
                previousState.copy(
                    loadingCurrentProduct = false,
                    isIdle = false,
                    editingProduct = false,
                    addingNewProduct = false,
                    isProductDeleting = true,
                    hasInvalidField = false,
                    navigateBack = true
                ) to ProductEffect.NavigateBack
            }
            is ProductEvent.UpdateIdleState -> {
                previousState.copy(
                    loadingCurrentProduct = false,
                    isIdle = true,
                    editingProduct = false,
                    addingNewProduct = false,
                    isProductDeleting = false,
                    hasInvalidField = false,
                    navigateBack = false
                ) to null
            }
            is ProductEvent.UpdateInvalidField -> {
                previousState.copy(
                    hasInvalidField = true
                ) to ProductEffect.InvalidFieldErrorMessage
            }
            is ProductEvent.OnBackClick -> {
                previousState.copy(
                    isIdle = false,
                    navigateBack = true
                ) to ProductEffect.NavigateBack
            }
        }
    }
}