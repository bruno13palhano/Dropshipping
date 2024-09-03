package com.bruno13palhano.product.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.product.R
import com.bruno13palhano.product.ui.shared.ProductsEffect
import com.bruno13palhano.product.ui.viewmodel.ProductsViewModel
import com.bruno13palhano.ui.components.clickableWithoutRipple
import com.bruno13palhano.ui.components.CommonItem
import com.bruno13palhano.ui.components.CustomIntegerField
import com.bruno13palhano.ui.components.CustomTextField
import com.bruno13palhano.ui.components.ElevatedListItem
import com.bruno13palhano.ui.components.rememberFlowWithLifecycle

@Composable
internal fun ProductsRoute(
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getProducts() }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(flow = viewModel.effect)

    val errorMessage = stringResource(id = R.string.empty_fields_error)
    val deleteMessage = stringResource(id = R.string.product_deleted)

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(effect) {
        effect.collect { action ->
            when(action) {
                is ProductsEffect.ShowErrorMessage -> {
                    snackbarHostState.showSnackbar(message = errorMessage)
                }
                is ProductsEffect.ShowDeletedMessage -> {
                    snackbarHostState.showSnackbar(message = deleteMessage)
                }
            }
        }
    }

    ProductsContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        products = state.products,
        onEditProductItemClick = viewModel::updatingProductState,
        onDeleteItemClick = viewModel::deleteProduct,
        onAddNewProductClick = viewModel::addButtonClick
    )

    AnimatedVisibility(
        visible = state.isProductUpdating || state.isProductAdding,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        var title = ""
        if (state.isProductUpdating) title = stringResource(id = R.string.update_product)
        else if(state.isProductAdding) title = stringResource(id = R.string.add_product)

        ProductContent(
            modifier = Modifier
                .clickableWithoutRipple {
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                }
                .padding(16.dp)
                .fillMaxSize(),
            title = title,
            naturaCode = viewModel.naturaCode,
            productName = viewModel.productName,
            hasInvalidField = state.hasInvalidField,
            onNaturaCodeChange = viewModel::updateNaturaCode,
            onProductNameChange = viewModel::updateProductName,
            onOkClick = viewModel::okButtonClick,
            onCancelClick = viewModel::cancelButtonClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    products: List<CommonItem>,
    onEditProductItemClick: (id: Long) -> Unit,
    onDeleteItemClick: (id: Long) -> Unit,
    onAddNewProductClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .semantics { contentDescription = "Products content" }
            .consumeWindowInsets(WindowInsets.statusBars)
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.products)) }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics { contentDescription = "Add button" },
                onClick = onAddNewProductClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_product)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .semantics { contentDescription = "List of products" }
                .consumeWindowInsets(it),
            contentPadding = it
        ) {
            items(items = products, key = { product -> product.id }) { product ->
                ElevatedListItem(
                    modifier = Modifier.padding(4.dp),
                    icon = Icons.Filled.Delete,
                    iconDescription = stringResource(id = R.string.delete),
                    onItemClick = { onEditProductItemClick(product.id) },
                    onIconClick = { onDeleteItemClick(product.id) }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = product.title
                    )
                }
            }
        }
    }
}

@Composable
internal fun ProductContent(
    modifier: Modifier = Modifier,
    title: String,
    naturaCode: String,
    productName: String,
    hasInvalidField: Boolean,
    onNaturaCodeChange: (String) -> Unit,
    onProductNameChange: (String) -> Unit,
    onOkClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.semantics { contentDescription = "Product Content" }
    ) {
        OutlinedCard {
            ElevatedCard {
                Text(
                    modifier = Modifier
                        .semantics { contentDescription = "Title" }
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                CustomIntegerField(
                    modifier = Modifier
                        .semantics { contentDescription = "Natura code" }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .fillMaxWidth(),
                    value = naturaCode,
                    onValueChange = onNaturaCodeChange,
                    label = stringResource(id = R.string.natura_code),
                    placeholder = stringResource(id = R.string.enter_natura_code),
                    isError = hasInvalidField && naturaCode.isBlank(),
                )

                CustomTextField(
                    modifier = Modifier
                        .semantics { contentDescription = "Product name" }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .fillMaxWidth(),
                    value = productName,
                    onValueChange = onProductNameChange,
                    label = stringResource(id = R.string.product_name),
                    placeholder = stringResource(id = R.string.enter_product_name),
                    isError = hasInvalidField && productName.isBlank()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier
                            .semantics { contentDescription = "Ok button" }
                            .padding(start = 8.dp, top = 8.dp, end = 4.dp, bottom = 8.dp),
                        onClick = onOkClick
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                    Button(
                        modifier = Modifier
                            .semantics { contentDescription = "Cancel button" }
                            .padding(start = 4.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                        onClick = onCancelClick
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductContentPreview() {
    ProductContent(
        title = stringResource(id = R.string.add_product),
        naturaCode = "1234",
        productName = "",
        hasInvalidField = true,
        onNaturaCodeChange = {},
        onProductNameChange = {},
        onOkClick = {},
        onCancelClick = {}
    )
}

@Preview
@Composable
private fun ProductsContentPreview() {
    ProductsContent(
        snackbarHostState = remember { SnackbarHostState() },
        products = listOf(
            CommonItem(id = 1, title = "product 1"),
            CommonItem(id = 2, title = "product 2"),
            CommonItem(id = 3, title = "product 3"),
            CommonItem(id = 4, title = "product 4"),
            CommonItem(id = 5, title = "product 5"),
            CommonItem(id = 6, title = "product 6"),
        ),
        onEditProductItemClick = {},
        onDeleteItemClick = {},
        onAddNewProductClick = {}
    )
}