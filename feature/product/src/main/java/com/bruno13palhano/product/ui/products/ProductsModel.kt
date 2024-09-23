package com.bruno13palhano.product.ui.products

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class ProductsState(
    val editProduct: Boolean,
    val addProduct: Boolean,
    val products: List<CommonItem>
) : ViewState {
    companion object {
        val INITIAL_STATE = ProductsState(
            editProduct = false,
            addProduct = false,
            products = emptyList()
        )
    }
}

@Immutable
internal sealed interface ProductsEvent : ViewEvent {
    data class EditProduct(val editProduct: Boolean, val id: Long) : ProductsEvent
    data class AddProduct(val addProduct: Boolean) : ProductsEvent
}

@Immutable
internal sealed interface ProductsEffect : ViewEffect {
    data class NavigateToEditProduct(val id: Long) : ProductsEffect
    data object NavigateToAddProduct : ProductsEffect
}

@Immutable
internal sealed interface ProductsAction : ViewAction {
    data class OnEditProductClick(val editProduct: Boolean, val id: Long) : ProductsAction
    data class OnAddProductClick(val addProduct: Boolean) : ProductsAction
}