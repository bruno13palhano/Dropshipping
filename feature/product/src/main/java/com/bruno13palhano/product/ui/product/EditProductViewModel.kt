package com.bruno13palhano.product.ui.product

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class EditProductViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    val productFields: ProductFields = ProductFields()
) : BaseViewModel<EditProductState, EditProductAction, EditProductEvent, EditProductEffect>(
    actionProcessor = EditProductActionProcessor()
) {
    @Composable
    override fun states(events: Flow<EditProductEvent>): EditProductState {
        return editProductPresenter(
            productFields = productFields,
            productRepository = productRepository,
            events = events,
            sendEffect = ::sendEffect
        )
    }

    fun onAction(action: EditProductAction) {
        sendEvent(event = actionProcessor.processAction(viewAction = action))
    }
}