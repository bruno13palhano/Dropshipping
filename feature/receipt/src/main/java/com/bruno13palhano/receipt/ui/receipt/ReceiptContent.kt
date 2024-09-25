package com.bruno13palhano.receipt.ui.receipt

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bruno13palhano.receipt.R
import com.bruno13palhano.ui.components.CustomClickField
import com.bruno13palhano.ui.components.CustomFloatField
import com.bruno13palhano.ui.components.CustomIntegerField
import com.bruno13palhano.ui.components.CustomTextField
import com.bruno13palhano.ui.components.dateFormat
import com.bruno13palhano.ui.shared.stringToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptContent(
    modifier: Modifier,
    hasInvalidField: Boolean,
    receiptFields: ReceiptFields,
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val focusManager = LocalFocusManager.current
    var datePickerState = rememberDatePickerState()
    var showDateDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        CustomTextField(
            modifier = Modifier
                .semantics { contentDescription = "Product name" }
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            value = receiptFields.productName,
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
            value = receiptFields.requestNumber,
            onValueChange = receiptFields::updateRequestNumber,
            label = stringResource(id = R.string.request_number),
            placeholder = stringResource(id = R.string.enter_request_number),
            isError = hasInvalidField && receiptFields.requestNumber.isBlank()
        )
        CustomClickField(
            modifier = Modifier
                .semantics { contentDescription = "Request date" }
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            value = dateFormat.format(receiptFields.requestDate),
            onClick = { showDateDialog = true },
            label = stringResource(id = R.string.request_date),
            placeholder = stringResource(id = R.string.enter_request_date),
        )
        CustomTextField(
            modifier = Modifier
                .semantics { contentDescription = "Customer name" }
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            value = receiptFields.customerName,
            onValueChange = receiptFields::updateCustomerName,
            label = stringResource(id = R.string.customer_name),
            placeholder = stringResource(id = R.string.enter_customer_name),
            isError = hasInvalidField && receiptFields.customerName.isBlank()
        )
        CustomIntegerField(
            modifier = Modifier
                .semantics { contentDescription = "Quantity" }
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            value = receiptFields.quantity,
            onValueChange = receiptFields::updateQuantity,
            label = stringResource(id = R.string.quantity),
            placeholder = stringResource(id = R.string.enter_quantity),
            isError = hasInvalidField && (receiptFields.quantity.isBlank()
                    || stringToInt(receiptFields.quantity) <= 0)
        )
        CustomFloatField(
            modifier = Modifier
                .semantics { contentDescription = "Natura price" }
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            value = receiptFields.naturaPrice,
            onValueChange = receiptFields::updateNaturaPrice,
            label = stringResource(id = R.string.natura_price),
            placeholder = stringResource(id = R.string.enter_natura_price),
            isError = hasInvalidField && receiptFields.naturaPrice.isBlank()
        )
        CustomFloatField(
            modifier = Modifier
                .semantics { contentDescription = "Amazon price" }
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            value = receiptFields.amazonPrice,
            onValueChange = receiptFields::updateAmazonPrice,
            label = stringResource(id = R.string.amazon_price),
            placeholder = stringResource(id = R.string.enter_amazon_price),
            isError = hasInvalidField && receiptFields.amazonPrice.isBlank()
        )
        CustomTextField(
            modifier = Modifier
                .semantics { contentDescription = "Payment option" }
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            value = receiptFields.paymentOption,
            onValueChange = receiptFields::updatePaymentOption,
            label = stringResource(id = R.string.payment_option),
            placeholder = stringResource(id = R.string.enter_payment_option),
            isError = hasInvalidField && receiptFields.paymentOption.isBlank()
        )
        CustomTextField(
            modifier = Modifier
                .semantics { contentDescription = "Observations" }
                .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            value = receiptFields.observations,
            onValueChange = receiptFields::updateObservations,
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
                            receiptFields.updateRequestDate(requestDate = newRequestDate)
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
                initialSelectedDateMillis = receiptFields.requestDate,
                initialDisplayMode = if (isPortrait) DisplayMode.Picker else DisplayMode.Picker
            )

            DatePicker(
                state = datePickerState,
                showModeToggle = isPortrait
            )
        }
    }
}