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
    actionProcessor = EditProductActionProcessor(),
    reducer = EditProductReducer(productFields = productFields)
) {
    @Composable
    override fun states(events: Flow<EditProductEvent>): EditProductState {
        return editProductPresenter(
            productFields = productFields,
            productRepository = productRepository,
            reducer = reducer,
            events = events,
            sendEffect = ::sendEffect
        )
    }
}