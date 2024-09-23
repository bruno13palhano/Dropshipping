package com.bruno13palhano.product.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.model.Product
import com.bruno13palhano.product.ui.product.ProductAction
import com.bruno13palhano.product.ui.product.ProductActionProcessor
import com.bruno13palhano.product.ui.product.ProductEffect
import com.bruno13palhano.product.ui.product.ProductEvent
import com.bruno13palhano.product.ui.product.ProductState
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    val productFields: ProductFields = ProductFields()
) : BaseViewModel<ProductState, ProductAction, ProductEvent, ProductEffect>(
    actionProcessor = ProductActionProcessor()
) {
    fun getProduct(id: Long) {
        if (id == 0L) return

        sendEvent(event = ProductEvent.UpdateLoadingCurrentProduct)

        viewModelScope.launch {
            productRepository.get(id = id)
                .catch { it.printStackTrace() }
                .collect {
                    productFields.updateNaturaCode(naturaCode = it.naturaCode)
                    productFields.updateProductName(productName = it.name)
                    sendEvent(event = ProductEvent.UpdateCurrentProduct(product = it))
                    sendEvent(event = ProductEvent.UpdateIdleState)
                }
        }
    }

    fun saveProduct(id: Long) {
        if (!isProductValid()) return

        if (id == 0L) {
            sendEvent(event = ProductEvent.UpdateAddingNewProduct)
        } else {
            sendEvent(event = ProductEvent.UpdateEditingProduct(id = id))
        }
    }

    fun editProduct(id: Long) {
        viewModelScope.launch {
            productRepository.update(mapPropertiesToProduct(id = id))
            sendEvent(event = ProductEvent.OnEditProductSuccessfully)
        }
    }

    fun addNewProduct() {
        viewModelScope.launch {
            productRepository.insert(mapPropertiesToProduct())
            sendEvent(event = ProductEvent.OnAddNewProductSuccessfully)
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            productRepository.delete(id = id)
            sendEvent(event = ProductEvent.OnDeleteProductSuccessfully)
        }
    }

    private fun isProductValid(): Boolean {
        val isValid = productFields.naturaCode.isNotBlank()
                && productFields.productName.isNotBlank()

        sendEvent(event = ProductEvent.UpdateInvalidField(hasInvalidField = !isValid))

        return isValid
    }

    private fun mapPropertiesToProduct(id: Long = 0L): Product {
        return Product(
            id = id,
            naturaCode = productFields.naturaCode,
            name = productFields.productName
        )
    }

    @Composable
    override fun states(events: Flow<ProductEvent>): ProductState {
        TODO("Not yet implemented")
    }
}