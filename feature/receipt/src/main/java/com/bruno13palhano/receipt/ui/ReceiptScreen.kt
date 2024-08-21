package com.bruno13palhano.receipt.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.receipt.R
import com.bruno13palhano.receipt.ui.viewmodel.ReceiptViewModel
import com.bruno13palhano.ui.clearFocusOnKeyboardDismiss
import com.bruno13palhano.ui.clickableWithoutRipple
import com.bruno13palhano.ui.components.CustomClickField
import com.bruno13palhano.ui.components.CustomFloatField
import com.bruno13palhano.ui.components.CustomIntegerField
import com.bruno13palhano.ui.components.CustomTextField
import com.bruno13palhano.ui.components.MoreVertMenu
import com.bruno13palhano.ui.currentDate
import com.bruno13palhano.ui.dateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddReceiptRoute(
    modifier: Modifier = Modifier,
    productId: Long,
    onBackClick: () -> Unit,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getProduct(productId = productId) }
    LaunchedEffect(key1 = Unit) { viewModel.updateRequestDate(currentDate()) }

    val snackbarHostState = remember { SnackbarHostState() }
    var datePickerState = rememberDatePickerState()
    var showDateDialog by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current

    ReceiptContent(
        modifier = modifier,
        screenTitle = stringResource(id = R.string.add_receipt),
        snackbarHostState = snackbarHostState,
        menuItems = emptyList(),
        productName = viewModel.productName,
        requestNumber = viewModel.requestNumber,
        requestDate = dateFormat.format(viewModel.requestDate),
        customerName = viewModel.customerName,
        quantity = viewModel.quantity,
        naturaPrice = viewModel.naturaPrice,
        amazonPrice = viewModel.amazonPrice,
        paymentOption = viewModel.paymentOption,
        observations = viewModel.observations,
        onRequestNumberChange = viewModel::updateRequestNumber,
        onRequestDateClick = { showDateDialog = true },
        onCustomerNameChange = viewModel::updateCustomerName,
        onQuantityChange = viewModel::updateQuantity,
        onNaturaPriceChange = viewModel::updateNaturaPrice,
        onAmazonPriceChange = viewModel::updateAmazonPrice,
        onPaymentOptionChange = viewModel::updatePaymentOption,
        onObservationsChange = viewModel::updateObservations,
        onMoreVertMenuItemClick = {},
        onBackClick = onBackClick,
        onDoneClick = {
            viewModel.addReceipt()
            onBackClick()
        }
    )

    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = {
                focusManager.clearFocus(force = true)
                showDateDialog = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.updateRequestDate(it)
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
                initialSelectedDateMillis = viewModel.requestDate,
                initialDisplayMode = if (isPortrait) DisplayMode.Picker else DisplayMode.Picker
            )

            DatePicker(
                state = datePickerState,
                showModeToggle = isPortrait
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateReceiptRoute(
    modifier: Modifier = Modifier,
    id: Long,
    onBackClick: () -> Unit,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getReceipt(id = id) }

    val snackbarHostState = remember { SnackbarHostState() }
    var datePickerState = rememberDatePickerState()
    var showDateDialog by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current

    val items = listOf(
        stringResource(id = R.string.delete),
        stringResource(id = R.string.cancel)
    )

    ReceiptContent(
        modifier = modifier,
        screenTitle = stringResource(id = R.string.update_receipt),
        snackbarHostState = snackbarHostState,
        menuItems = items,
        productName = viewModel.productName,
        requestNumber = viewModel.requestNumber,
        requestDate = dateFormat.format(viewModel.requestDate),
        customerName = viewModel.customerName,
        quantity = viewModel.quantity,
        naturaPrice = viewModel.naturaPrice,
        amazonPrice = viewModel.amazonPrice,
        paymentOption = viewModel.paymentOption,
        observations = viewModel.observations,
        onRequestNumberChange = viewModel::updateRequestNumber,
        onRequestDateClick = { showDateDialog = true },
        onCustomerNameChange = viewModel::updateCustomerName,
        onQuantityChange = viewModel::updateQuantity,
        onNaturaPriceChange = viewModel::updateNaturaPrice,
        onAmazonPriceChange = viewModel::updateAmazonPrice,
        onPaymentOptionChange = viewModel::updatePaymentOption,
        onObservationsChange = viewModel::updateObservations,
        onMoreVertMenuItemClick = { index ->
            when (index) {
                ReceiptMenuOptions.DELETE -> {
                    viewModel.deleteReceipt()
                    onBackClick()
                }
                ReceiptMenuOptions.CANCEL -> {
                    viewModel.cancelReceipt()
                    onBackClick()
                }
                else -> {}
            }
        },
        onBackClick = onBackClick,
        onDoneClick = {
            viewModel.updateReceipt()
            onBackClick()
        }
    )

    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = {
                focusManager.clearFocus(force = true)
                showDateDialog = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.updateRequestDate(it)
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
                initialSelectedDateMillis = viewModel.requestDate,
                initialDisplayMode = if (isPortrait) DisplayMode.Picker else DisplayMode.Picker
            )

            DatePicker(
                state = datePickerState,
                showModeToggle = isPortrait
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptContent(
    modifier: Modifier = Modifier,
    screenTitle: String,
    snackbarHostState: SnackbarHostState,
    menuItems: List<String>,
    productName: String,
    requestNumber: String,
    requestDate: String,
    customerName: String,
    quantity: String,
    naturaPrice: String,
    amazonPrice: String,
    paymentOption: String,
    observations: String,
    onRequestNumberChange: (String) -> Unit,
    onRequestDateClick: () -> Unit,
    onCustomerNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onNaturaPriceChange: (String) -> Unit,
    onAmazonPriceChange: (String) -> Unit,
    onPaymentOptionChange: (String) -> Unit,
    onObservationsChange: (String) -> Unit,
    onMoreVertMenuItemClick: (index: Int) -> Unit,
    onDoneClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .clickableWithoutRipple {
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
            }
            .clearFocusOnKeyboardDismiss(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back)
                        )
                    }
                },
                actions = {
                    if (menuItems.isNotEmpty()) {
                        IconButton(onClick = { expanded = true }) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = stringResource(id = R.string.vert_menu)
                                )
                                Column(
                                    modifier = Modifier.fillMaxSize(),
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
            FloatingActionButton(onClick = onDoneClick) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = productName,
                onValueChange = {},
                label = "",
                placeholder = "",
                readOnly = true
            )
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = requestNumber,
                onValueChange = onRequestNumberChange,
                label = stringResource(id = R.string.request_number),
                placeholder = stringResource(id = R.string.enter_request_number)
            )
            CustomClickField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = requestDate,
                onClick = onRequestDateClick,
                label = stringResource(id = R.string.request_date),
                placeholder = stringResource(id = R.string.enter_request_date)
            )
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = customerName,
                onValueChange = onCustomerNameChange,
                label = stringResource(id = R.string.customer_name),
                placeholder = stringResource(id = R.string.enter_customer_name)
            )
            CustomIntegerField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = quantity,
                onValueChange = onQuantityChange,
                label = stringResource(id = R.string.quantity),
                placeholder = stringResource(id = R.string.enter_quantity)
            )
            CustomFloatField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = naturaPrice,
                onValueChange = onNaturaPriceChange,
                label = stringResource(id = R.string.natura_price),
                placeholder = stringResource(id = R.string.enter_natura_price)
            )
            CustomFloatField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = amazonPrice,
                onValueChange = onAmazonPriceChange,
                label = stringResource(id = R.string.amazon_price),
                placeholder = stringResource(id = R.string.enter_amazon_price)
            )
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = paymentOption,
                onValueChange = onPaymentOptionChange,
                label = stringResource(id = R.string.payment_option),
                placeholder = stringResource(id = R.string.enter_payment_option)
            )
            CustomTextField(
                modifier = Modifier
                    .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .weight(1f),
                value = observations,
                onValueChange = onObservationsChange,
                label = stringResource(id = R.string.observations),
                placeholder = stringResource(id = R.string.enter_observations),
                singleLine = false
            )
        }
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
        screenTitle = "Add Receipt",
        snackbarHostState = remember { SnackbarHostState() },
        menuItems = listOf("Delete", "Cancel"),
        productName = "Product 1",
        requestNumber = "1234",
        requestDate = "23/09/2023",
        customerName = "Customer 1",
        quantity = "1",
        naturaPrice = "10.0",
        amazonPrice = "20.0",
        paymentOption = "Cash",
        observations = "Sale canceled by the customer",
        onRequestNumberChange = {},
        onRequestDateClick = {},
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