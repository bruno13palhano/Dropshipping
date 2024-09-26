package com.bruno13palhano.product.ui.products

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class ProductsViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository
): BaseViewModel<ProductsState, ProductsAction, ProductsEvent, ProductsEffect>(
    actionProcessor = ProductsActionProcessor()
) {
    @Composable
    override fun states(events: Flow<ProductsEvent>): ProductsState {
        return productsPresenter(
            products = productRepository.getAll(),
            events = events,
            sendEffect = ::sendEffect
        )
    }

    fun onAction(action: ProductsAction) {
        sendEvent(event = actionProcessor.processAction(viewAction = action))
    }
}