package com.bruno13palhano.product.ui.product

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.product.R
import com.bruno13palhano.ui.components.clickableWithoutRipple
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun NewProductRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: NewProductViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val effect = rememberFlowWithLifecycle(viewModel.effects)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val errorMessage = stringResource(id = R.string.empty_fields_error)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(effect) {
        effect.collect { action ->
            when (action) {
                NewProductEffect.NavigateBack -> onBackClick()

                NewProductEffect.InvalidFieldErrorMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errorMessage,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }

    NewProductContent(
        modifier = modifier.clickableWithoutRipple {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        snackbarHostState = snackbarHostState,
        productFields = viewModel.productFields,
        hasInvalidField = state.hasInvalidField,
        onAction = { viewModel.onAction(action = it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewProductContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    productFields: ProductFields,
    hasInvalidField: Boolean,
    onAction: (action: NewProductAction) -> Unit
) {
    Scaffold(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .semantics { contentDescription = "Product Content" },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.add_product)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(NewProductAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(NewProductAction.OnDoneClick) }) {
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