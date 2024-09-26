package com.bruno13palhano.product.ui.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

@Composable
internal fun editProductPresenter(
    productFields: ProductFields,
    productRepository: ProductRepository,
    events: Flow<EditProductEvent>,
    sendEffect: (effect: EditProductEffect) -> Unit
): EditProductState {
    var hasInvalidField by remember { mutableStateOf(false) }
    var id by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is EditProductEvent.SetInitialData -> id = event.id

                is EditProductEvent.DeleteEditProduct -> {
                    deleteProduct(
                        id = id,
                        productRepository = productRepository
                    )

                    sendEffect(EditProductEffect.NavigateBack)
                }

                is EditProductEvent.Done -> {
                    if (!productFields.isValid()) {
                        hasInvalidField = true
                    } else {
                        hasInvalidField = false

                        updateProduct(
                            id = id,
                            productFields = productFields,
                            productRepository = productRepository
                        )

                        sendEffect(EditProductEffect.NavigateBack)
                    }
                }

                is EditProductEvent.NavigateBack -> sendEffect(EditProductEffect.NavigateBack)
            }
        }
    }

    LaunchedEffect(hasInvalidField) {
        if (hasInvalidField) sendEffect(EditProductEffect.InvalidFieldErrorMessage)
    }

    LaunchedEffect(id) {
        if (id == 0L) return@LaunchedEffect

        getProduct(
            id = id,
            productFields = productFields,
            productRepository = productRepository
        )
    }

    return EditProductState(hasInvalidField = hasInvalidField)
}

private suspend fun updateProduct(
    id: Long,
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    productRepository.update(data = productFields.toProduct(id = id))
}

private suspend fun deleteProduct(
    id: Long,
    productRepository: ProductRepository
) {
    productRepository.delete(id = id)
}

private suspend fun getProduct(
    id: Long,
    productFields: ProductFields,
    productRepository: ProductRepository
) {
    productRepository.get(id = id)
        .catch { it.printStackTrace() }
        .collect {
            productFields.setFields(product = it)
        }
}