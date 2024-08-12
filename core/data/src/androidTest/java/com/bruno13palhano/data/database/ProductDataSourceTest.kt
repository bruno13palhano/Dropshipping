package com.bruno13palhano.data.database

import app.cash.turbine.test
import com.bruno13palhano.data.internal.dao.ProductDao
import com.bruno13palhano.data.internal.database.AppDatabase
import com.bruno13palhano.data.internal.entity.ProductInternal
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
internal class ProductDataSourceTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: AppDatabase
    private lateinit var productDao: ProductDao

    @Before
    fun setup() {
        hiltRule.inject()

        productDao = database.productDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }


    @Test
    fun getProductsShouldReturnListOfAllProducts() = runTest {
        val productInternal1 = ProductInternal(id = 1L, naturaCode = "1", name = "Product 1")
        val productInternal2 = ProductInternal(id = 2L, naturaCode = "2", name = "Product 2")
        val productInternal3 = ProductInternal(id = 3L, naturaCode = "3", name = "Product 3")

        productDao.insert(productInternal1)
        productDao.insert(productInternal2)
        productDao.insert(productInternal3)

        val expected = listOf(productInternal3, productInternal2, productInternal1)

        productDao.getAll().test {
            assertThat(awaitItem()).containsExactlyElementsIn(expected).inOrder()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun searchProductsByNaturaCodeShouldReturnListOfProductsThatMatchTheQuery() = runTest {
        val productInternal1 = ProductInternal(id = 1L, naturaCode = "1", name = "Product 1")
        val productInternal2 = ProductInternal(id = 2L, naturaCode = "2", name = "Product 2")
        val productInternal3 = ProductInternal(id = 3L, naturaCode = "3", name = "Product 3")

        productDao.insert(productInternal1)
        productDao.insert(productInternal2)
        productDao.insert(productInternal3)

        productDao.search(query = productInternal1.naturaCode).test {
            assertThat(awaitItem()).containsExactly(productInternal1)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun searchProductsByNameShouldReturnListOfProductsThatMatchTheQuery() = runTest {
        val productInternal1 = ProductInternal(id = 1L, naturaCode = "1", name = "Product 1")
        val productInternal2 = ProductInternal(id = 2L, naturaCode = "2", name = "Product 2")
        val productInternal3 = ProductInternal(id = 3L, naturaCode = "3", name = "Product 3")

        productDao.insert(productInternal1)
        productDao.insert(productInternal2)
        productDao.insert(productInternal3)

        productDao.search(query = productInternal2.name).test {
            assertThat(awaitItem()).containsExactly(productInternal2)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun shouldReturn_ListOfProducts_InDescOrderByNameAndNaturaCode_WhenThatMatchTheQuery() = runTest {
        val commonNameAndCode = "product2"
        val productInternal1 = ProductInternal(id = 1L, naturaCode = "1", name = "Product 1")
        val productInternal2 = ProductInternal(id = 2L, naturaCode = "2", name = commonNameAndCode)
        val productInternal3 = ProductInternal(
            id = 3L,
            naturaCode = commonNameAndCode,
            name = "Product 3"
        )
        val productInternal4 = ProductInternal(
            id = 4L,
            naturaCode = "1",
            name = commonNameAndCode
        )

        productDao.insert(productInternal1)
        productDao.insert(productInternal2)
        productDao.insert(productInternal3)
        productDao.insert(productInternal4)

        val expected = listOf(productInternal3, productInternal2, productInternal4)

        productDao.search(query = commonNameAndCode).test {
            assertThat(awaitItem()).containsExactlyElementsIn(expected).inOrder()
            cancelAndConsumeRemainingEvents()
        }
    }
}