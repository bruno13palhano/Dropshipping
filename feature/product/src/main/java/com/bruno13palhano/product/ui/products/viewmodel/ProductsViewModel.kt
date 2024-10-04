package com.bruno13palhano.product.ui.products.viewmodel

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.product.ui.products.presenter.ProductsAction
import com.bruno13palhano.product.ui.products.presenter.ProductsActionProcessor
import com.bruno13palhano.product.ui.products.presenter.ProductsEffect
import com.bruno13palhano.product.ui.products.presenter.ProductsEvent
import com.bruno13palhano.product.ui.products.presenter.ProductsReducer
import com.bruno13palhano.product.ui.products.presenter.ProductsState
import com.bruno13palhano.product.ui.products.presenter.productsPresenter
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class ProductsViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository
): BaseViewModel<ProductsState, ProductsAction, ProductsEvent, ProductsEffect>(
    actionProcessor = ProductsActionProcessor(),
    reducer = ProductsReducer()
) {
    @Composable
    override fun states(events: Flow<ProductsEvent>): ProductsState {
        return productsPresenter(
            products = productRepository.getAll(),
            events = events,
            reducer = reducer,
            sendEvent = ::sendEvent,
            sendEffect = ::sendEffect
        )
    }
}