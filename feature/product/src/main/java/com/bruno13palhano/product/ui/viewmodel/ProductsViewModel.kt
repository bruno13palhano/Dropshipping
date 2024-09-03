package com.bruno13palhano.product.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.model.Product
import com.bruno13palhano.product.ui.shared.ProductsEffect
import com.bruno13palhano.product.ui.shared.ProductsEvent
import com.bruno13palhano.product.ui.shared.ProductsReducer
import com.bruno13palhano.product.ui.shared.ProductsState
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductsViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository
): BaseViewModel<ProductsState, ProductsEvent, ProductsEffect>(
    initialState = ProductsState.INITIAL_STATE,
    reducer = ProductsReducer()
) {
    private var productId by mutableLongStateOf(0L)
    var naturaCode by mutableStateOf("")
        private set
    var productName by mutableStateOf("")
        private set

    fun updateNaturaCode(value: String) { naturaCode = value }

    fun updateProductName(value: String) { productName = value }

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getAll().collect { productList: List<Product> ->
                sendEvent(
                    event = ProductsEvent.UpdateProducts(
                        isUpdating = true,
                        products = productList.map { product ->
                            CommonItem(
                                id = product.id,
                                title = product.name
                            )
                        }
                    )
                )
            }
        }
    }

    fun updatingProductState(id: Long) {
        viewModelScope.launch {
            productRepository.get(id = id).collect {
                setSelectedProductProperties(it)
            }
        }

        sendEvent(
            event = ProductsEvent.ProductUpdating(
                isUpdating = true,
                product = Product(id = id, naturaCode = naturaCode, name = productName)
            )
        )
    }

    fun addButtonClick() {
        resetSelectedProductProperties()
        sendEvent(event = ProductsEvent.ProductAdding(true))
    }

    fun cancelButtonClick() {
        sendEvent(event = ProductsEvent.IdleState(true))
    }

    fun okButtonClick() {
        if (state.value.isProductUpdating) {
            if (!isProductValid()) {
                sendEvent(event = ProductsEvent.ProductUpdatingInvalidField(true))
                return
            }

            viewModelScope.launch { productRepository.update(data = saveProduct(id = productId)) }
            sendEvent(event = ProductsEvent.IdleState(true))
        } else if (state.value.isProductAdding) {
            if (!isProductValid()) {
                sendEvent(event = ProductsEvent.ProductAddingInvalidField(true))
                return
            }

            viewModelScope.launch { productRepository.insert(data = saveProduct()) }
            sendEvent(event = ProductsEvent.IdleState(true))
        }
    }

    fun deleteProduct(id: Long) {
        sendEvent(event = ProductsEvent.ProductDeleting(true))

        viewModelScope.launch {
            try {
                productRepository.delete(id = id)
            } catch (_: Exception) {}
        }
        sendEvent(event = ProductsEvent.IdleState(true))
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

    private fun resetSelectedProductProperties() {
        productId = 0L
        naturaCode = ""
        productName = ""
    }

    private fun setSelectedProductProperties(product: Product) {
        productId = product.id
        naturaCode = product.naturaCode
        productName = product.name
    }
}