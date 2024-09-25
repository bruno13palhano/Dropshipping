package com.bruno13palhano.product.ui.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

@Composable
internal fun newProductPresenter(
    productFields: ProductFields,
    productRepository: ProductRepository,
    events: Flow<NewProductEvent>,
    sendEffect: (effect: NewProductEffect) -> Unit
): NewProductState {
    var hasInvalidField by remember { mutableStateOf(false) }
    var done by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is NewProductEvent.Done -> {
                    if (!productFields.isValid()) {
                        hasInvalidField = true
                    } else {
                        hasInvalidField = false
                        done = true
                    }
                }

                is NewProductEvent.NavigateBack -> {
                    sendEffect(NewProductEffect.NavigateBack)
                }
            }
        }
    }

    LaunchedEffect(done) {
        if (done) {
            insertProduct(productFields = productFields, productRepository = productRepository)

            sendEffect(NewProductEffect.NavigateBack)
        }
    }

    LaunchedEffect(hasInvalidField) {
        if (hasInvalidField) sendEffect(NewProductEffect.InvalidFieldErrorMessage)
    }

    return NewProductState(
        hasInvalidField = hasInvalidField
    )
}

private suspend fun insertProduct(
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    productRepository.insert(data = productFields.toProduct())
}