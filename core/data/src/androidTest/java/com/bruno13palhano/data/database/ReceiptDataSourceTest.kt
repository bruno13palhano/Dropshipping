package com.bruno13palhano.data.database

import app.cash.turbine.test
import com.bruno13palhano.data.internal.dao.ReceiptDao
import com.bruno13palhano.data.internal.database.AppDatabase
import com.bruno13palhano.data.internal.entity.ReceiptInternal
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@HiltAndroidTest
internal class ReceiptDataSourceTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: AppDatabase
    private lateinit var receiptDao: ReceiptDao

    @Before
    fun setup() {
        hiltRule.inject()

        receiptDao = database.receiptDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun shouldReturn_ListWithLastReceipt_ByTheLimit() = runTest {
        val limit = 3
        val receiptList = (1..10).map {
            ReceiptInternal(
                id = it.toLong(),
                productId = it.toLong(),
                requestNumber = 12234L,
                requestDate = 1234454L,
                quantity = it,
                customerName = "",
                naturaPrice = 0.0f,
                amazonPrice = 0.0f,
                paymentOption = "",
                canceled = false,
                observations = ""
            )
        }

        receiptList.forEach { receiptDao.insert(it) }

        val expected = receiptList.takeLast(limit).reversed()

        receiptDao.getLastReceipts(limit = limit).test {
            assertThat(awaitItem()).containsExactlyElementsIn(expected).inOrder()
            cancelAndConsumeRemainingEvents()
        }
    }
}