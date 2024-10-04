package com.bruno13palhano.product.ui.products.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.ViewAction
import com.bruno13palhano.ui.shared.ViewEffect
import com.bruno13palhano.ui.shared.ViewEvent
import com.bruno13palhano.ui.shared.ViewState

@Immutable
internal data class ProductsState(val products: List<CommonItem>) : ViewState {
    companion object {
        val INITIAL_STATE = ProductsState(products = emptyList())
    }
}

@Immutable
internal sealed interface ProductsEvent : ViewEvent {
    data class EditProduct(val id: Long) : ProductsEvent
    data object AddProduct : ProductsEvent
    data class UpdateProducts(val products: List<CommonItem>) : ProductsEvent
}

@Immutable
internal sealed interface ProductsEffect : ViewEffect {
    data class NavigateToEditProduct(val id: Long) : ProductsEffect
    data object NavigateToAddProduct : ProductsEffect
}

@Immutable
internal sealed interface ProductsAction : ViewAction {
    data class OnEditProductClick(val id: Long) : ProductsAction
    data object OnAddProductClick : ProductsAction
}