package com.bruno13palhano.product.ui.product.presenter

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.product.R
import com.bruno13palhano.product.ui.product.viewmodel.EditProductViewModel
import com.bruno13palhano.ui.components.clickableWithoutRipple
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun EditProductRoute(
    modifier: Modifier,
    id: Long,
    onBackClick: () -> Unit,
    viewModel: EditProductViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.onAction(EditProductAction.OnSetInitialData(id = id))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.effects)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val errorMessage = stringResource(id = R.string.empty_fields_error)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(effect) {
        effect.collect { action ->
            when(action) {
                is EditProductEffect.InvalidFieldErrorMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errorMessage,
                            withDismissAction = true
                        )
                    }
                }

                is EditProductEffect.NavigateBack -> onBackClick()
            }
        }
    }

    EditProductContent(
        modifier = modifier.clickableWithoutRipple {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        snackbarHostState = snackbarHostState,
        productFields = viewModel.productFields,
        hasInvalidField = state.hasInvalidField,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProductContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    productFields: ProductFields,
    hasInvalidField: Boolean,
    onAction: (action: EditProductAction) -> Unit
) {
    Scaffold(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .semantics { contentDescription = "Product Content" },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.update_product)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(EditProductAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction.invoke(EditProductAction.OnDeleteClick) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(EditProductAction.OnDoneClick) }) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done)
                )
            }
        }
    ) {
        ProductContent(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it),
            productFields = productFields,
            hasInvalidField = hasInvalidField
        )
    }
}

@Preview
@Composable
private fun EditProductContentPreview() {
    EditProductContent(
        snackbarHostState = remember { SnackbarHostState() },
        productFields = ProductFields(),
        hasInvalidField = true,
        onAction = {}
    )
}