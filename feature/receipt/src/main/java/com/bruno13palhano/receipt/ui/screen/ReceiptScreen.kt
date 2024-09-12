package com.bruno13palhano.receipt.ui.screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.receipt.R
import com.bruno13palhano.receipt.ui.shared.ReceiptEffect
import com.bruno13palhano.receipt.ui.shared.ReceiptEvent
import com.bruno13palhano.receipt.ui.viewmodel.ReceiptViewModel
import com.bruno13palhano.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.ui.components.clickableWithoutRipple
import com.bruno13palhano.ui.components.CustomClickField
import com.bruno13palhano.ui.components.CustomFloatField
import com.bruno13palhano.ui.components.CustomIntegerField
import com.bruno13palhano.ui.components.CustomTextField
import com.bruno13palhano.ui.components.MoreVertMenu
import com.bruno13palhano.ui.components.currentDate
import com.bruno13palhano.ui.components.dateFormat
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun ReceiptRoute(
    modifier: Modifier = Modifier,
    id: Long,
    productId: Long,
    onBackClick: () -> Unit,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    val title = getScreenTitle(receiptId = id)

    LaunchedEffect(key1 = Unit) {
        getInitialData(
            receiptId = id,
            productId = productId,
            viewModel = viewModel
        )
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effect)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)

    LaunchedEffect(key1 = effect) {
        effect.collect { effect ->
            when (effect) {
                is ReceiptEffect.UpdateReceipt -> viewModel.updateReceipt(id = effect.id)

                is ReceiptEffect.InsertReceipt -> viewModel.insertReceipt()

                is ReceiptEffect.DeleteReceipt -> viewModel.deleteReceipt(id = effect.id)

                is ReceiptEffect.CancelReceipt -> viewModel.cancelReceipt(id = effect.id)

                is ReceiptEffect.NavigateBack -> onBackClick()

                is ReceiptEffect.InvalidFieldErrorMessage -> {
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

    ReceiptContent(
        modifier = modifier,
        showMenu = id != 0L,
        snackbarHostState = snackbarHostState,
        screenTitle = title,
        menuItems = items,
        hasInvalidField = state.hasInvalidField,
        productName = viewModel.productName,
        requestNumber = viewModel.requestNumber,
        requestDate = viewModel.requestDate,
        customerName = viewModel.customerName,
        quantity = viewModel.quantity,
        naturaPrice = viewModel.naturaPrice,
        amazonPrice = viewModel.amazonPrice,
        paymentOption = viewModel.paymentOption,
        observations = viewModel.observations,
        onRequestNumberChange = viewModel::updateRequestNumber,
        onRequestDateChange = viewModel::updateRequestDate,
        onCustomerNameChange = viewModel::updateCustomerName,
        onQuantityChange = viewModel::updateQuantity,
        onNaturaPriceChange = viewModel::updateNaturaPrice,
        onAmazonPriceChange = viewModel::updateAmazonPrice,
        onPaymentOptionChange = viewModel::updatePaymentOption,
        onObservationsChange = viewModel::updateObservations,
        onMoreVertMenuItemClick = { index ->
            when (index) {
                ReceiptMenuOptions.DELETE -> {
                    viewModel.sendEvent(event = ReceiptEvent.UpdateDeleteReceipt(id = id))
                }

                ReceiptMenuOptions.CANCEL -> {
                    viewModel.sendEvent(event = ReceiptEvent.CancelReceipt(id = id))
                }

                else -> {}
            }
        },
        onBackClick = { viewModel.sendEvent(event = ReceiptEvent.OnNavigateBack) },
        onDoneClick = { viewModel.saveReceipt(id = id) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptContent(
    modifier: Modifier = Modifier,
    showMenu: Boolean,
    snackbarHostState: SnackbarHostState,
    screenTitle: String,
    menuItems: List<String>,
    hasInvalidField: Boolean,
    productName: String,
    requestNumber: String,
    requestDate: Long,
    customerName: String,
    quantity: String,
    naturaPrice: String,
    amazonPrice: String,
    paymentOption: String,
    observations: String,
    onRequestNumberChange: (requestNumber: String) -> Unit,
    onRequestDateChange: (requestDate: Long) -> Unit,
    onCustomerNameChange: (customerName: String) -> Unit,
    onQuantityChange: (quantity: String) -> Unit,
    onNaturaPriceChange: (naturaPrice: String) -> Unit,
    onAmazonPriceChange: (amazonPrice: String) -> Unit,
    onPaymentOptionChange: (paymentOption: String) -> Unit,
    onObservationsChange: (observations: String) -> Unit,
    onMoreVertMenuItemClick: (index: Int) -> Unit,
    onDoneClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var datePickerState = rememberDatePickerState()
    var expanded by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }

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
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.semantics { contentDescription = "Navigate back" },
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                },
                actions = {
                    if (showMenu) {
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
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics { contentDescription = "Add button" },
                onClick = onDoneClick
            ) {
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
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            CustomTextField(
                modifier = Modifier
                    .semantics { contentDescription = "Product name" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = productName,
                onValueChange = {},
                label = "",
                placeholder = "",
                readOnly = true
            )
            CustomIntegerField(
                modifier = Modifier
                    .semantics { contentDescription = "Request number" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = requestNumber,
                onValueChange = onRequestNumberChange,
                label = stringResource(id = R.string.request_number),
                placeholder = stringResource(id = R.string.enter_request_number),
                isError = hasInvalidField && requestNumber.isBlank()
            )
            CustomClickField(
                modifier = Modifier
                    .semantics { contentDescription = "Request date" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = dateFormat.format(requestDate),
                onClick = { showDateDialog = true },
                label = stringResource(id = R.string.request_date),
                placeholder = stringResource(id = R.string.enter_request_date),
            )
            CustomTextField(
                modifier = Modifier
                    .semantics { contentDescription = "Customer name" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = customerName,
                onValueChange = onCustomerNameChange,
                label = stringResource(id = R.string.customer_name),
                placeholder = stringResource(id = R.string.enter_customer_name),
                isError = hasInvalidField && customerName.isBlank()
            )
            CustomIntegerField(
                modifier = Modifier
                    .semantics { contentDescription = "Quantity" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = quantity,
                onValueChange = onQuantityChange,
                label = stringResource(id = R.string.quantity),
                placeholder = stringResource(id = R.string.enter_quantity),
                isError = hasInvalidField && quantity.isBlank()
            )
            CustomFloatField(
                modifier = Modifier
                    .semantics { contentDescription = "Natura price" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = naturaPrice,
                onValueChange = onNaturaPriceChange,
                label = stringResource(id = R.string.natura_price),
                placeholder = stringResource(id = R.string.enter_natura_price),
                isError = hasInvalidField && naturaPrice.isBlank()
            )
            CustomFloatField(
                modifier = Modifier
                    .semantics { contentDescription = "Amazon price" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = amazonPrice,
                onValueChange = onAmazonPriceChange,
                label = stringResource(id = R.string.amazon_price),
                placeholder = stringResource(id = R.string.enter_amazon_price),
                isError = hasInvalidField && amazonPrice.isBlank()
            )
            CustomTextField(
                modifier = Modifier
                    .semantics { contentDescription = "Payment option" }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = paymentOption,
                onValueChange = onPaymentOptionChange,
                label = stringResource(id = R.string.payment_option),
                placeholder = stringResource(id = R.string.enter_payment_option),
                isError = hasInvalidField && paymentOption.isBlank()
            )
            CustomTextField(
                modifier = Modifier
                    .semantics { contentDescription = "Observations" }
                    .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                value = observations,
                onValueChange = onObservationsChange,
                label = stringResource(id = R.string.observations),
                placeholder = stringResource(id = R.string.enter_observations),
                singleLine = false
            )
        }

        AnimatedVisibility(
            visible = showDateDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DatePickerDialog(
                modifier = Modifier.semantics { contentDescription = "Date picker dialog" },
                onDismissRequest = {
                    focusManager.clearFocus(force = true)
                    showDateDialog = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { newRequestDate ->
                                onRequestDateChange(newRequestDate)
                            }
                            focusManager.clearFocus(force = true)
                            showDateDialog = false
                        }
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            focusManager.clearFocus(force = true)
                            showDateDialog = false
                        }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            ) {
                datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = requestDate,
                    initialDisplayMode = if (isPortrait) DisplayMode.Picker else DisplayMode.Picker
                )

                DatePicker(
                    state = datePickerState,
                    showModeToggle = isPortrait
                )
            }
        }
    }
}

@Composable
private fun getScreenTitle(receiptId: Long): String {
    return if (receiptId != 0L) stringResource(id = R.string.update_receipt)
        else stringResource(id = R.string.add_receipt)
}

private fun getInitialData(
    receiptId: Long,
    productId: Long,
    viewModel: ReceiptViewModel
) {
    if (receiptId == 0L) {
        viewModel.getProduct(productId = productId)
        viewModel.updateRequestDate(currentDate())
    } else {
        viewModel.getReceipt(id = receiptId)
    }
}

private object ReceiptMenuOptions {
    const val DELETE = 0
    const val CANCEL = 1
}

@Preview
@Composable
private fun ReceiptPreview() {
    ReceiptContent(
        showMenu = true,
        snackbarHostState = SnackbarHostState(),
        screenTitle = "Update Receipt",
        menuItems = listOf("Delete", "Cancel"),
        hasInvalidField = true,
        productName = "Product 1",
        requestNumber = "1234",
        requestDate = 74624272627L,
        customerName = "Customer 1",
        quantity = "1",
        naturaPrice = "10.0",
        amazonPrice = "20.0",
        paymentOption = "",
        observations = "Sale canceled by the customer",
        onRequestNumberChange = {},
        onRequestDateChange = {},
        onCustomerNameChange = {},
        onQuantityChange = {},
        onNaturaPriceChange = {},
        onAmazonPriceChange = {},
        onPaymentOptionChange = {},
        onObservationsChange = {},
        onMoreVertMenuItemClick = {},
        onBackClick = {},
        onDoneClick = {}
    )
}