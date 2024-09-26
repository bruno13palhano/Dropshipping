package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ReceiptRepository
import com.bruno13palhano.model.Product
import kotlinx.coroutines.flow.Flow

@Composable
internal fun newReceiptPresenter(
    receiptFields: ReceiptFields,
    productRepository: ProductRepository,
    receiptRepository: ReceiptRepository,
    events: Flow<NewReceiptEvent>,
    sendEffect: (effect: NewReceiptEffect) -> Unit
): NewReceiptState {
    val currentProduct = remember { mutableStateOf(Product.EMPTY) }
    var hasInvalidField by remember { mutableStateOf(false) }
    var currentProductId by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is NewReceiptEvent.SetInitialData -> {
                    currentProductId = event.productId
                    receiptFields.updateRequestDate(requestDate = event.requestDate)
                }

                is NewReceiptEvent.Done -> {
                    if (!receiptFields.isReceiptValid()) {
                        hasInvalidField = true
                    } else {
                        hasInvalidField = false

                        insertReceipt(
                            receiptRepository = receiptRepository,
                            receiptFields = receiptFields,
                            product = currentProduct.value
                        )

                        sendEffect(NewReceiptEffect.NavigateBack)
                    }
                }

                is NewReceiptEvent.NavigateBack -> {
                    sendEffect(NewReceiptEffect.NavigateBack)
                }
            }
        }
    }

    LaunchedEffect(currentProductId) {
        if (currentProductId == 0L) return@LaunchedEffect

        getProduct(
            productId = currentProductId,
            product = currentProduct,
            productRepository = productRepository,
            receiptFields = receiptFields
        )
    }

    LaunchedEffect(hasInvalidField) {
        if (hasInvalidField) sendEffect(NewReceiptEffect.InvalidFieldErrorMessage)
    }

    return NewReceiptState(hasInvalidField = hasInvalidField)
}

private suspend fun insertReceipt(
    receiptRepository: ReceiptRepository,
    receiptFields: ReceiptFields,
    product: Product
) {
    receiptRepository.insert(data = receiptFields.toReceipt(product = product))
}

private suspend fun getProduct(
    productId: Long,
    product: MutableState<Product>,
    productRepository: ProductRepository,
    receiptFields: ReceiptFields
) {
    productRepository.get(id = productId).collect {
        product.value = it

        receiptFields.updateProductName(productName = it.name)
    }
}