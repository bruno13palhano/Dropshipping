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
                    isIdle = false
                ) to null
            }

            is ProductEvent.UpdateCurrentProduct -> {
                previousState.copy(
                    loadingCurrentProduct = true,
                    isIdle = false,
                    currentProduct = event.product
                ) to null
            }

            is ProductEvent.UpdateEditingProduct -> {
                previousState.copy(
                    editingProduct = true
                ) to ProductEffect.EditProduct(id = event.id)
            }

            is ProductEvent.UpdateAddingNewProduct -> {
                previousState.copy(
                    addingNewProduct = true
                ) to ProductEffect.AddNewProduct
            }

            is ProductEvent.UpdateDeletingProduct -> {
                previousState.copy(
                    isProductDeleting = true
                ) to ProductEffect.DeleteProduct(event.id)
            }

            is ProductEvent.OnEditProductSuccessfully -> {
                previousState.copy(
                    hasInvalidField = false,
                    navigateBack = true
                ) to ProductEffect.NavigateBack
            }

            is ProductEvent.OnAddNewProductSuccessfully -> {
                previousState.copy(
                    hasInvalidField = false,
                    navigateBack = true
                ) to ProductEffect.NavigateBack
            }

            is ProductEvent.OnDeleteProductSuccessfully -> {
                previousState.copy(navigateBack = true) to ProductEffect.NavigateBack
            }

            is ProductEvent.UpdateIdleState -> {
                previousState.copy(isIdle = true) to null
            }

            is ProductEvent.UpdateInvalidField -> {
                val effect =
                    if (event.hasInvalidField) ProductEffect.InvalidFieldErrorMessage
                    else null

                previousState.copy(
                    hasInvalidField = event.hasInvalidField
                ) to effect
            }

            is ProductEvent.OnBackClick -> {
                previousState.copy(navigateBack = true) to ProductEffect.NavigateBack
            }
        }
    }
}