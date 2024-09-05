package com.bruno13palhano.product.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.model.Product
import com.bruno13palhano.product.ui.shared.ProductEffect
import com.bruno13palhano.product.ui.shared.ProductEvent
import com.bruno13palhano.product.ui.shared.ProductReducer
import com.bruno13palhano.product.ui.shared.ProductState
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository
) : BaseViewModel<ProductState, ProductEvent, ProductEffect>(
    initialState = ProductState.INITIAL_STATE,
    reducer = ProductReducer()
) {
    private var productId by mutableLongStateOf(0L)
    var naturaCode by mutableStateOf("")
        private set
    var productName by mutableStateOf("")
        private set

    fun updateNaturaCode(value: String) { naturaCode = value }

    fun updateProductName(value: String) { productName = value }

    fun getProduct(id: Long) {
        if (id == 0L) return

        sendEvent(event = ProductEvent.UpdateLoadingCurrentProduct)

        viewModelScope.launch {
            productRepository.get(id = id)
                .catch { it.printStackTrace() }
                .collect {
                    productId = id
                    naturaCode = it.naturaCode
                    productName = it.name
                    sendEvent(event = ProductEvent.UpdateCurrentProduct(product = it))
                }
        }

        sendEvent(event = ProductEvent.UpdateIdleState)
    }

    fun onDoneClick() {
        viewModelScope.launch {
            if (isProductValid()) {
                if (productId == 0L) {
                    sendEvent(event = ProductEvent.UpdateAddingNewProduct)
                    productRepository.insert(saveProduct())
                    sendEvent(event = ProductEvent.OnAddingNewProductDoneClick)
                } else {
                    sendEvent(event = ProductEvent.UpdateEditingProduct)
                    productRepository.update(saveProduct(id = productId))
                    sendEvent(event = ProductEvent.OnEditingProductDoneClick)
                }
            } else {
                sendEvent(event = ProductEvent.UpdateInvalidField)
            }
        }
    }

    fun onDeleteClick() {
        sendEvent(event = ProductEvent.UpdateDeletingProduct)

        viewModelScope.launch { productRepository.delete(id = productId) }

        sendEvent(event = ProductEvent.OnDeleteClick)
    }

    fun onBackClick() {
        sendEvent(event = ProductEvent.OnBackClick)
    }

    private fun isProductValid(): Boolean {
        return naturaCode.isNotBlank() && productName.isNotBlank()
    }

    private fun saveProduct(id: Long = 0L): Product {
        return Product(
            id = id,
            naturaCode = naturaCode,
            name = productName
        )
    }
}