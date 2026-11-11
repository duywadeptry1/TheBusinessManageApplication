package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.SeedData
import com.example.data.repository.BusinessRepository
import com.example.model.OrderStatus
import com.example.viewmodel.GoShipViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: BusinessRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = BusinessRepository(database.businessDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Business Manage", appName)
    }

    @Test
    fun `test Room database seeding and JSON import`() = runBlocking {
        repository.ensureSeeded()
        val initialOrders = repository.ordersFlow.first()
        assertEquals(5, initialOrders.size)

        // Import sample JSON batch into Room database
        val importSummary = repository.importFromJson(SeedData.SAMPLE_IMPORT_JSON)
        assertNull(importSummary.errorMessage)
        assertEquals(3, importSummary.ordersImported)
        assertEquals(7, importSummary.revenuesImported)

        val updatedOrders = repository.ordersFlow.first()
        assertEquals(8, updatedOrders.size)
        assertTrue(updatedOrders.any { it.trackingNumber == "VN-99201-HN" })
    }

    @Test
    fun `test Room database CSV import and status update`() = runBlocking {
        repository.ensureSeeded()
        val csvSummary = repository.importFromCsv(SeedData.SAMPLE_IMPORT_CSV)
        assertNull(csvSummary.errorMessage)
        assertEquals(3, csvSummary.ordersImported)

        val ordersAfterCsv = repository.ordersFlow.first()
        assertEquals(8, ordersAfterCsv.size)

        // Update project workflow status in Room DB
        repository.updateOrderStatus("ORD-001", OrderStatus.COMPLETED)
        val updatedOrder = repository.ordersFlow.first().find { it.id == "ORD-001" }
        assertNotNull(updatedOrder)
        assertEquals(OrderStatus.COMPLETED, updatedOrder?.status)
    }

    @Test
    fun `test Business Manage workflow processes and GPS check-in`() {
        val viewModel = GoShipViewModel()
        val initialCount = viewModel.uiState.value.orders.size
        assertFalse(viewModel.uiState.value.isCheckedInToday)

        // Process 1: Create new Business Project
        viewModel.createOrder(
            recipientName = "Trần Thu Trang",
            recipientAddress = "Dự án ERP Chi nhánh Miền Bắc",
            weightKg = 10.0f,
            paymentMethod = "TECH"
        )

        val updatedState = viewModel.uiState.value
        assertEquals(initialCount + 1, updatedState.orders.size)
        assertEquals(OrderStatus.PENDING, updatedState.orders.first().status)
        assertEquals("Trần Thu Trang", updatedState.orders.first().recipientName)

        // Process 2: GPS Attendance Check-in
        viewModel.performGpsCheckIn()
        assertTrue(viewModel.uiState.value.isCheckedInToday)
        assertEquals(125, viewModel.uiState.value.attendanceCount)
    }

    @Test
    fun `test dark mode toggle`() {
        val viewModel = GoShipViewModel()
        viewModel.toggleDarkMode(true)
        assertEquals(true, viewModel.uiState.value.isDarkMode)

        viewModel.toggleDarkMode(false)
        assertEquals(false, viewModel.uiState.value.isDarkMode)
    }
}
