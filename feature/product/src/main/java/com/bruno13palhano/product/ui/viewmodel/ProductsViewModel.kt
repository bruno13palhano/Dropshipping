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

    private var productId by mutableLongStateOf(0L)
    var naturaCode by mutableStateOf("")
        private set
    var productName by mutableStateOf("")
        private set

    fun updateNaturaCode(value: String) {
        naturaCode = value
    }

    fun updateProductName(value: String) {
        productName = value
    }

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getAll().collect {
                _products.update { it }
            }
        }
    }

    fun setUpdateProductState(id: Long)  {
        viewModelScope.launch {
            productRepository.get(id = id).collect {
                productId = it.id
                naturaCode = it.naturaCode
                productName = it.name
            }
        }
        _uiState.update { UiState.Updating }
    }

    fun setAddProductState() {
        _uiState.update { UiState.Adding }
    }

    fun updateProduct() {
        viewModelScope.launch {
            productRepository.update(
                data = Product(
                    id = productId,
                    naturaCode = naturaCode,
                    name = productName
                )
            )
        }
        _uiState.update { UiState.Idle }
    }

    fun addProduct() {
        viewModelScope.launch {
            productRepository.insert(
                data = Product(
                    id =  0L,
                    naturaCode = naturaCode,
                    name = productName
                )
            )
        }
        _uiState.update { UiState.Idle }
    }
}

sealed interface UiState {
    data object  Idle : UiState
    data object  Adding : UiState
    data object  Updating : UiState
}