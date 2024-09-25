package com.bruno13palhano.product.ui.product

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class NewProductViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    val productFields: ProductFields
): BaseViewModel<NewProductState, NewProductAction, NewProductEvent, NewProductEffect>(
    actionProcessor = NewProductActionProcessor()
) {
    @Composable
    override fun states(events: Flow<NewProductEvent>): NewProductState {
        return newProductPresenter(
            productFields = productFields,
            productRepository = productRepository,
            events = events,
            sendEffect = ::sendEffect
        )
    }

    fun onAction(action: NewProductAction) {
        sendEvent(event = actionProcessor.processAction(viewAction = action))
    }
}