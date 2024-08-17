package com.bruno13palhano.product.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository
): ViewModel() {
    private var _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private var _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = UiState.Idle
        )

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
                _products.update {
                    productList
                }
            }
        }
    }

    fun setUpdateProductState(id: Long)  {
        if (_uiState.value == UiState.Adding) return

        viewModelScope.launch {
            productRepository.get(id = id).collect {
                setSelectedProductProperties(it)
            }
        }
        _uiState.update { UiState.Updating }
    }

    fun setAddProductState() {
        if (_uiState.value == UiState.Updating) return

        resetSelectedProductProperties()
        _uiState.update { UiState.Adding }
    }

    fun setCancelState() = _uiState.update { UiState.Idle }

    fun updateProduct() {
        if (!isProductValid()) {
            _uiState.update { UiState.Error }
            return
        }

        viewModelScope.launch { productRepository.update(data = saveProduct(id = productId)) }
        _uiState.update { UiState.Idle }
    }

    fun addProduct() {
        if (!isProductValid()) {
            _uiState.update { UiState.Error }
            return
        }

        viewModelScope.launch { productRepository.insert(data = saveProduct()) }
        _uiState.update { UiState.Idle }
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

sealed interface UiState {
    data object Idle : UiState
    data object Adding : UiState
    data object Updating : UiState
    data object Error : UiState
}