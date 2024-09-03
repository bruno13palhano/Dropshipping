package com.bruno13palhano.product.ui.shared

import androidx.compose.runtime.Immutable
import com.bruno13palhano.model.Product
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.Reducer

@Immutable
internal data class ProductsState(
    val isProductsUpdating: Boolean,
    val isProductUpdating: Boolean,
    val isProductAdding: Boolean,
    val isProductCanceling: Boolean,
    val isProductDeleting: Boolean,
    val hasInvalidField: Boolean,
    val isIdle: Boolean,
    val isError: Boolean,
    val currentProduct: Product,
    val products: List<CommonItem>
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = ProductsState(
            isProductsUpdating = false,
            isProductUpdating = false,
            isProductAdding = false,
            isProductCanceling = false,
            isProductDeleting = false,
            hasInvalidField = false,
            isIdle = true,
            isError = false,
            currentProduct = Product(0L,"",""),
            products = emptyList()
        )
    }
}

@Immutable
internal sealed interface ProductsEvent : Reducer.ViewEvent {
    data class ProductUpdating(val isUpdating: Boolean, val product: Product) : ProductsEvent
    data class ProductAdding(val isAdding: Boolean) : ProductsEvent
    data class ProductCanceling(val isCanceling: Boolean) : ProductsEvent
    data class ProductDeleting(val isDeleting: Boolean) : ProductsEvent
    data class ProductUpdatingInvalidField(val hasInvalidField: Boolean) : ProductsEvent
    data class ProductAddingInvalidField(val hasInvalidField: Boolean) : ProductsEvent
    data class IdleState(val isIdle: Boolean) : ProductsEvent
    data class ErrorState(val isError: Boolean) : ProductsEvent
    data class UpdateProducts(val isUpdating: Boolean, val products: List<CommonItem>) : ProductsEvent
}

@Immutable
internal sealed interface ProductsEffect : Reducer.ViewEffect {
    data object ShowErrorMessage : ProductsEffect
    data object ShowDeletedMessage : ProductsEffect
}