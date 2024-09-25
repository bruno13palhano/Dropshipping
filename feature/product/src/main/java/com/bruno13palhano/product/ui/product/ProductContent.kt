package com.bruno13palhano.product.ui.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bruno13palhano.product.R
import com.bruno13palhano.ui.components.CustomIntegerField
import com.bruno13palhano.ui.components.CustomTextField

@Composable
internal fun ProductContent(
    modifier: Modifier,
    productFields: ProductFields,
    hasInvalidField: Boolean
) {
    Column(modifier = modifier) {
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