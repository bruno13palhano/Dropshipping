package com.bruno13palhano.product.ui.product.viewmodel

import androidx.compose.runtime.Composable
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.product.ui.product.presenter.NewProductAction
import com.bruno13palhano.product.ui.product.presenter.NewProductActionProcessor
import com.bruno13palhano.product.ui.product.presenter.NewProductEffect
import com.bruno13palhano.product.ui.product.presenter.NewProductEvent
import com.bruno13palhano.product.ui.product.presenter.NewProductReducer
import com.bruno13palhano.product.ui.product.presenter.NewProductState
import com.bruno13palhano.product.ui.product.presenter.ProductFields
import com.bruno13palhano.product.ui.product.presenter.newProductPresenter
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class NewProductViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    val productFields: ProductFields
): BaseViewModel<NewProductState, NewProductAction, NewProductEvent, NewProductEffect>(
    actionProcessor = NewProductActionProcessor(),
    reducer = NewProductReducer(productFields = productFields)
) {
    @Composable
    override fun states(events: Flow<NewProductEvent>): NewProductState {
        return newProductPresenter(
            productFields = productFields,
            productRepository = productRepository,
            reducer = reducer,
            events = events,
            sendEffect = ::sendEffect
        )
    }
}