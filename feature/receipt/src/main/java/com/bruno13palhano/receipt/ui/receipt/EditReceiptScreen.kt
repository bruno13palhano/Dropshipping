package com.bruno13palhano.receipt.ui.receipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.bruno13palhano.ui.components.MoreVertMenu
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun EditReceiptRoute(
    modifier: Modifier = Modifier,
    id: Long,
    onBackClick: () -> Unit,
    viewModel: EditReceiptViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(action = EditReceiptAction.OnUpdateReceipt(id = id))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effects)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)

    LaunchedEffect(key1 = effect) {
        effect.collect { action ->
            when (action) {
                is EditReceiptEffect.NavigateBack -> onBackClick()

                is EditReceiptEffect.InvalidFieldErrorMessage -> {
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

    val items = listOf(
        stringResource(id = R.string.delete),
        stringResource(id = R.string.cancel)
    )

    EditReceiptContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        menuItems = items,
        hasInvalidField = state.hasInvalidField,
        receiptFields = viewModel.receiptFields,
        onMoreVertMenuItemClick = { index ->
            when (index) {
                ReceiptMenuOptions.DELETE -> {
                    viewModel.onAction(action = EditReceiptAction.OnDeleteClick)
                }

                ReceiptMenuOptions.CANCEL -> {
                    viewModel.onAction(action = EditReceiptAction.OnCancelClick)
                }

                else -> {}
            }
        },
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditReceiptContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    menuItems: List<String>,
    hasInvalidField: Boolean,
    receiptFields: ReceiptFields,
    onMoreVertMenuItemClick: (index: Int) -> Unit,
    onAction: (action: EditReceiptAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }

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
                title = { Text(text = stringResource(id = R.string.receipt)) },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.semantics { contentDescription = "Navigate back" },
                        onClick = { onAction(EditReceiptAction.OnBackClick) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        modifier = Modifier.semantics { contentDescription = "More vert menu" },
                        onClick = { expanded = true }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.vert_menu)
                            )
                            Column(
                                modifier = Modifier
                                    .semantics { contentDescription = "More vert menu items" }
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MoreVertMenu(
                                    items = menuItems,
                                    expanded = expanded,
                                    onDismissRequest = { expanded = it },
                                    onItemClick = onMoreVertMenuItemClick
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics { contentDescription = "Add button" },
                onClick = { onAction(EditReceiptAction.OnDoneClick) }
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
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
            hasInvalidField = hasInvalidField,
            receiptFields = receiptFields
        )
    }
}

private object ReceiptMenuOptions {
    const val DELETE = 0
    const val CANCEL = 1
}

@Preview
@Composable
private fun EditReceiptPreview() {
    EditReceiptContent(
        snackbarHostState = SnackbarHostState(),
        menuItems = listOf("Delete", "Cancel"),
        hasInvalidField = true,
        receiptFields = ReceiptFields(),
        onMoreVertMenuItemClick = {},
        onAction = {}
    )
}