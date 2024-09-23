package com.bruno13palhano.product.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.product.R
import com.bruno13palhano.product.ui.shared.ProductEffect
import com.bruno13palhano.product.ui.shared.ProductEvent
import com.bruno13palhano.product.ui.viewmodel.ProductFields
import com.bruno13palhano.product.ui.viewmodel.ProductViewModel
import com.bruno13palhano.ui.components.CustomIntegerField
import com.bruno13palhano.ui.components.CustomTextField
import com.bruno13palhano.ui.components.clickableWithoutRipple
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun ProductRoute(
    modifier: Modifier,
    id: Long,
    onBackClick: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getProduct(id = id) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.effects)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val errorMessage = stringResource(id = R.string.empty_fields_error)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val title = getScreenTitle(productId = id)

    LaunchedEffect(effect) {
        effect.collect { action ->
            when(action) {
                is ProductEffect.EditProduct -> viewModel.editProduct(id = action.id)

                is ProductEffect.AddNewProduct -> viewModel.addNewProduct()

                is ProductEffect.DeleteProduct -> viewModel.deleteProduct(id = action.id)

                is ProductEffect.InvalidFieldErrorMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errorMessage,
                            withDismissAction = true
                        )
                    }
                }

                is ProductEffect.NavigateBack -> onBackClick()
            }
        }
    }

    ProductContent(
        modifier = modifier.clickableWithoutRipple {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        snackbarHostState = snackbarHostState,
        showEditButton = id != 0L,
        title = title,
        productFields = viewModel.productFields,
        hasInvalidField = state.hasInvalidField,
        onDoneClick = { viewModel.saveProduct(id = id) },
        onDeleteClick = {
            viewModel.sendEvent(event = ProductEvent.UpdateDeletingProduct(id = id))
        },
        onBackClick = {
            viewModel.sendEvent(event = ProductEvent.OnBackClick)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    showEditButton: Boolean,
    title: String,
    productFields: ProductFields,
    hasInvalidField: Boolean,
    onDoneClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .semantics { contentDescription = "Product Content" },
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                },
                actions = {
                    if (showEditButton) {
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(id = R.string.delete)
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onDoneClick) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it),
        ) {
            CustomIntegerField(
                modifier = Modifier
                    .semantics { contentDescription = "Natura code" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = productFields.naturaCode,
                onValueChange = productFields::updateNaturaCode,
                label = stringResource(id = R.string.natura_code),
                placeholder = stringResource(id = R.string.enter_natura_code),
                isError = hasInvalidField && productFields.naturaCode.isBlank(),
            )

            CustomTextField(
                modifier = Modifier
                    .semantics { contentDescription = "Product name" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = productFields.productName,
                onValueChange = productFields::updateProductName,
                label = stringResource(id = R.string.product_name),
                placeholder = stringResource(id = R.string.enter_product_name),
                isError = hasInvalidField && productFields.productName.isBlank()
            )
        }
    }
}

@Composable
private fun getScreenTitle(productId: Long): String {
    return if (productId != 0L) stringResource(id = R.string.update_product)
        else stringResource(id = R.string.add_product)
}

@Preview
@Composable
private fun ProductContentPreview() {
    ProductContent(
        snackbarHostState = remember { SnackbarHostState() },
        showEditButton = false,
        title = stringResource(id = R.string.add_product),
        productFields = ProductFields(),
        hasInvalidField = true,
        onDoneClick = {},
        onDeleteClick = {},
        onBackClick = {}
    )
}