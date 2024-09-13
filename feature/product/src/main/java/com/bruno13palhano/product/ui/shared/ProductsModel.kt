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
    val products: List<CommonItem>
) : Reducer.ViewState {
    companion object {
        val INITIAL_STATE = ProductsState(
            productsLoading = true,
            editProduct = false,
            addProduct = false,
            deleteProduct = false,
            products = emptyList()
        )
    }
}

@Immutable
internal sealed interface ProductsEvent : Reducer.ViewEvent {
    data class UpdateProducts(val isLoading: Boolean, val products: List<CommonItem>) : ProductsEvent
    data class EditProduct(val editProduct: Boolean, val id: Long) : ProductsEvent
    data class AddProduct(val addProduct: Boolean) : ProductsEvent
    data class UpdateDeletingProduct(val isDeleting: Boolean, val id: Long) : ProductsEvent
    data object OnDeleteProductSuccessfully : ProductsEvent
}

@Immutable
internal sealed interface ProductsEffect : Reducer.ViewEffect {
    data class NavigateToEditProduct(val id: Long) : ProductsEffect
    data object NavigateToAddProduct : ProductsEffect
    data class DeleteProduct(val id: Long) : ProductsEffect
    data object ShowDeletedMessage : ProductsEffect
}