package com.bruno13palhano.product.ui.product

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class ProductState(
    val loadingCurrentProduct: Boolean,
    val editingProduct: Boolean,
    val addingNewProduct: Boolean,
    val isProductDeleting: Boolean,
    val isIdle: Boolean,
    val hasInvalidField: Boolean,
    val navigateBack: Boolean,
    val currentProduct: Product
) : ViewState {
    companion object {
        val INITIAL_STATE = ProductState(
            loadingCurrentProduct = false,
            editingProduct = false,
            addingNewProduct = false,
            isProductDeleting = false,
            isIdle = true,
            hasInvalidField = false,
            navigateBack = false,
            currentProduct = Product.EMPTY
        )
    }
}

@Immutable
internal sealed interface ProductEvent : ViewEvent {
    data object UpdateLoadingCurrentProduct : ProductEvent
    data class UpdateCurrentProduct(val product: Product) : ProductEvent
    data class UpdateEditingProduct(val id: Long) : ProductEvent
    data class UpdateDeletingProduct(val id: Long) : ProductEvent
    data object UpdateAddingNewProduct : ProductEvent
    data object OnEditProductSuccessfully : ProductEvent
    data object OnAddNewProductSuccessfully : ProductEvent
    data object OnDeleteProductSuccessfully : ProductEvent
    data object UpdateIdleState : ProductEvent
    data class UpdateInvalidField(val hasInvalidField: Boolean) : ProductEvent
    data object OnBackClick : ProductEvent
}

@Immutable
internal sealed interface ProductEffect : ViewEffect {
    data class EditProduct(val id: Long) : ProductEffect
    data object AddNewProduct : ProductEffect
    data class DeleteProduct(val id: Long) : ProductEffect
    data object InvalidFieldErrorMessage : ProductEffect
    data object NavigateBack : ProductEffect
}

@Immutable
internal sealed interface ProductAction : ViewAction