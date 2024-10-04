package com.bruno13palhano.product.ui.products.presenter

import com.bruno13palhano.ui.shared.ActionProcessor

internal class ProductsActionProcessor : ActionProcessor<ProductsAction, ProductsEvent> {
    override fun processAction(viewAction: ProductsAction): ProductsEvent {
        return when (viewAction) {
            is ProductsAction.OnAddProductClick -> ProductsEvent.AddProduct

            is ProductsAction.OnEditProductClick -> ProductsEvent.EditProduct(id = viewAction.id)
        }
    }
}