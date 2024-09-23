package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.bruno13palhano.receipt.R
import com.bruno13palhano.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.ui.components.clickableWithoutRipple
import com.bruno13palhano.ui.components.currentDate
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun NewReceiptRoute(
    modifier: Modifier = Modifier,
    productId: Long,
    onBackClick: () -> Unit,
    viewModel: NewReceiptViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(
            action = NewReceiptAction.OnSetInitialData(
                productId = productId,
                requestDate = currentDate()
            )
        )
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effects)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)

    LaunchedEffect(key1 = effect) {
        effect.collect { action ->
            when (action) {
                is NewReceiptEffect.InvalidFieldErrorMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errorMessage,
                            withDismissAction = true
                        )
                    }
                }

                is NewReceiptEffect.NavigateBack -> onBackClick()
            }
        }
    }

    NewReceiptContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        hasInvalidField = state.hasInvalidField,
        receiptFields = viewModel.receiptFields,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewReceiptContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    hasInvalidField: Boolean,
    receiptFields: ReceiptFields,
    onAction: (action: NewReceiptAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = modifier
            .semantics { contentDescription = "Receipt content" }
            .clickableWithoutRipple {
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
            }
            .clearFocusOnKeyboardDismiss()
            .consumeWindowInsets(WindowInsets.safeDrawing),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.add_receipt)) },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.semantics { contentDescription = "Navigate back" },
                        onClick = { onAction(NewReceiptAction.OnBackClick) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics { contentDescription = "Add button" },
                onClick = { onAction(NewReceiptAction.OnDoneClick) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done)
                )
            }
        }
    ) {
        ReceiptContent(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            hasInvalidField = hasInvalidField,
            receiptFields = receiptFields
        )
    }
}

@Preview
@Composable
private fun NewReceiptPreview() {
    NewReceiptContent(
        snackbarHostState = SnackbarHostState(),
        hasInvalidField = true,
        receiptFields = ReceiptFields(),
        onAction = {}
    )
}