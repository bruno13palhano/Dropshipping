package com.bruno13palhano.product.ui.product.viewmodel

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.product.ui.product.presenter.EditProductAction
import com.bruno13palhano.product.ui.product.presenter.EditProductActionProcessor
import com.bruno13palhano.product.ui.product.presenter.EditProductEffect
import com.bruno13palhano.product.ui.product.presenter.EditProductEvent
import com.bruno13palhano.product.ui.product.presenter.EditProductReducer
import com.bruno13palhano.product.ui.product.presenter.EditProductState
import com.bruno13palhano.product.ui.product.presenter.ProductFields
import com.bruno13palhano.product.ui.product.presenter.editProductPresenter
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