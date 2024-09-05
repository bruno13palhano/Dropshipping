package com.bruno13palhano.product.ui.shared

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.Reducer

@Immutable
internal data class ProductsState(
    val productsLoading: Boolean,
    val editProduct: Boolean,
    val addProduct: Boolean,
    val deleteProduct: Boolean,
    val isIdle: Boolean,
    val isError: Boolean,
    val products: List<CommonItem>
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = ProductsState(
            productsLoading = true,
            editProduct = false,
            addProduct = false,
            deleteProduct = false,
            isIdle = false,
            isError = false,
            products = emptyList()
        )
    }
}

@Immutable
internal sealed interface ProductsEvent : Reducer.ViewEvent {
    data class UpdateProducts(val isLoading: Boolean, val products: List<CommonItem>) : ProductsEvent
    data class EditProduct(val editProduct: Boolean, val id: Long) : ProductsEvent
    data class AddProduct(val addProduct: Boolean) : ProductsEvent
    data class ProductDeleting(val isDeleting: Boolean) : ProductsEvent
    data class IdleState(val isIdle: Boolean) : ProductsEvent
    data class ErrorState(val isError: Boolean) : ProductsEvent
}

@Immutable
internal sealed interface ProductsEffect : Reducer.ViewEffect {
    data class NavigateToEditProduct(val id: Long) : ProductsEffect
    data object NavigateToAddProduct : ProductsEffect
    data object ShowErrorMessage : ProductsEffect
    data object ShowDeletedMessage : ProductsEffect
}

@Immutable
internal data class ProductState(
    val loadingCurrentProduct: Boolean,
    val editingProduct: Boolean,
    val addingNewProduct: Boolean,
    val isProductDeleting: Boolean,
    val isIdle: Boolean,
    val hasInvalidField: Boolean,
    val navigateBack: Boolean,
    val currentProduct: Product?
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = ProductState(
            loadingCurrentProduct = false,
            editingProduct = false,
            addingNewProduct = false,
            isProductDeleting = false,
            isIdle = true,
            hasInvalidField = false,
            navigateBack = false,
            currentProduct = null
        )
    }
}

@Immutable
internal sealed interface ProductEvent : Reducer.ViewEvent {
    data object UpdateLoadingCurrentProduct : ProductEvent
    data class UpdateCurrentProduct(val product: Product) : ProductEvent
    data object UpdateEditingProduct : ProductEvent
    data object UpdateDeletingProduct : ProductEvent
    data object UpdateAddingNewProduct : ProductEvent
    data object OnEditingProductDoneClick : ProductEvent
    data object OnAddingNewProductDoneClick : ProductEvent
    data object OnDeleteClick : ProductEvent
    data object UpdateIdleState : ProductEvent
    data object UpdateInvalidField : ProductEvent
    data object OnBackClick : ProductEvent
}

@Immutable
internal sealed interface ProductEffect : Reducer.ViewEffect {
    data object InvalidFieldErrorMessage : ProductEffect
    data object NavigateBack : ProductEffect
}