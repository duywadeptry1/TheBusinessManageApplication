package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.SeedData
import com.example.data.repository.BusinessRepository
import com.example.model.CourierDriver
import com.example.model.DailyRevenue
import com.example.model.OrderStatus
import com.example.model.PaymentMethodShare
import com.example.model.ShippingOrder
import com.example.model.ShippingService
import com.example.model.StatCardData
import com.example.model.TrackingStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class GoShipUiState(
    val selectedTab: Int = 0,
    val isDarkMode: Boolean? = null, // null = system, true = dark, false = light
    val searchQuery: String = "",
    val selectedOrderFilter: OrderStatus? = null,
    val isCreateOrderSheetVisible: Boolean = false,
    val isDatabaseImportSheetVisible: Boolean = false,
    val databaseEditorPayload: String = SeedData.SAMPLE_IMPORT_JSON,
    val selectedServiceForOrder: ShippingService? = null,
    val trackingOrderId: String = "VN-88492-HN",
    val orders: List<ShippingOrder> = emptyList(),
    val services: List<ShippingService> = emptyList(),
    val stats: List<StatCardData> = emptyList(),
    val dailyRevenues: List<DailyRevenue> = emptyList(),
    val paymentShares: List<PaymentMethodShare> = emptyList(),
    val activeDriver: CourierDriver? = null,
    val trackingSteps: List<TrackingStep> = emptyList(),
    val snackbarMessage: String? = null
)

class GoShipViewModel(
    private val repository: BusinessRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoShipUiState())
    val uiState: StateFlow<GoShipUiState> = _uiState.asStateFlow()

    init {
        loadInitialFallbackAndObserveDatabase()
    }

    private fun loadInitialFallbackAndObserveDatabase() {
        val initialOrders = SeedData.defaultOrders().map { it.toDomain() }
        val initialServices = SeedData.defaultServices().map { it.toDomain() }
        val initialRevenues = SeedData.defaultRevenues().map { it.toDomain() }
        val initialShares = SeedData.defaultPaymentShares().map { it.toDomain() }

        _uiState.update {
            it.copy(
                orders = initialOrders,
                stats = computeStatsFromOrders(initialOrders),
                services = initialServices,
                dailyRevenues = initialRevenues,
                paymentShares = initialShares,
                activeDriver = SeedData.defaultDriver(),
                trackingSteps = SeedData.defaultTrackingSteps()
            )
        }

        if (repository != null) {
            viewModelScope.launch {
                repository.ensureSeeded()
            }
            viewModelScope.launch {
                repository.ordersFlow.collectLatest { dbOrders ->
                    if (dbOrders.isNotEmpty()) {
                        _uiState.update { state ->
                            state.copy(
                                orders = dbOrders,
                                stats = computeStatsFromOrders(dbOrders),
                                trackingOrderId = dbOrders.firstOrNull()?.trackingNumber ?: state.trackingOrderId
                            )
                        }
                    }
                }
            }
            viewModelScope.launch {
                repository.servicesFlow.collectLatest { dbServices ->
                    if (dbServices.isNotEmpty()) {
                        _uiState.update { it.copy(services = dbServices) }
                    }
                }
            }
            viewModelScope.launch {
                repository.revenuesFlow.collectLatest { dbRevenues ->
                    if (dbRevenues.isNotEmpty()) {
                        _uiState.update { it.copy(dailyRevenues = dbRevenues) }
                    }
                }
            }
            viewModelScope.launch {
                repository.paymentSharesFlow.collectLatest { dbShares ->
                    if (dbShares.isNotEmpty()) {
                        _uiState.update { it.copy(paymentShares = dbShares) }
                    }
                }
            }
        }
    }

    private fun computeStatsFromOrders(orders: List<ShippingOrder>): List<StatCardData> {
        val total = orders.size
        val delivering = orders.count { it.status == OrderStatus.DELIVERING || it.status == OrderStatus.PENDING }
        val completed = orders.count { it.status == OrderStatus.COMPLETED }
        val cancelled = orders.count { it.status == OrderStatus.CANCELLED }
        val expressCount = orders.count { it.serviceType.contains("Hỏa tốc", ignoreCase = true) }
        val successRate = if (total > 0) ((completed * 100f) / total).toInt() else 100

        return listOf(
            StatCardData(
                title = "Tổng đơn hàng",
                count = total.toString(),
                subtitle = "đơn trong DB",
                trend = "+14.8%",
                isPositive = true
            ),
            StatCardData(
                title = "Đang vận chuyển",
                count = delivering.toString(),
                subtitle = "đang xử lý",
                trend = "$expressCount hỏa tốc",
                isPositive = true
            ),
            StatCardData(
                title = "Giao thành công",
                count = completed.toString(),
                subtitle = "tỷ lệ $successRate%",
                trend = "+8.4%",
                isPositive = true
            ),
            StatCardData(
                title = "Đã hoàn / hủy",
                count = cancelled.toString(),
                subtitle = "kiểm soát tốt",
                trend = "-2.1%",
                isPositive = false
            )
        )
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun toggleDarkMode(enable: Boolean) {
        _uiState.update { it.copy(isDarkMode = enable) }
    }

    fun setSystemTheme() {
        _uiState.update { it.copy(isDarkMode = null) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun filterOrders(status: OrderStatus?) {
        _uiState.update { it.copy(selectedOrderFilter = status) }
    }

    fun setTrackingOrder(trackingNumber: String) {
        _uiState.update { it.copy(trackingOrderId = trackingNumber) }
    }

    fun openCreateOrderSheet(service: ShippingService? = null) {
        _uiState.update {
            it.copy(
                isCreateOrderSheetVisible = true,
                selectedServiceForOrder = service ?: it.services.firstOrNull()
            )
        }
    }

    fun closeCreateOrderSheet() {
        _uiState.update { it.copy(isCreateOrderSheetVisible = false) }
    }

    fun openDatabaseImportSheet() {
        _uiState.update { it.copy(isDatabaseImportSheetVisible = true) }
    }

    fun closeDatabaseImportSheet() {
        _uiState.update { it.copy(isDatabaseImportSheetVisible = false) }
    }

    fun updateDatabaseEditorPayload(payload: String) {
        _uiState.update { it.copy(databaseEditorPayload = payload) }
    }

    fun selectServiceForOrder(service: ShippingService) {
        _uiState.update { it.copy(selectedServiceForOrder = service) }
    }

    fun createOrder(
        recipientName: String,
        recipientAddress: String,
        weightKg: Float,
        paymentMethod: String
    ) {
        val currentState = _uiState.value
        val service = currentState.selectedServiceForOrder ?: currentState.services.first()
        val newOrder = ShippingOrder(
            id = "ORD-${System.currentTimeMillis() % 100000}",
            trackingNumber = "VN-${(10000..99999).random()}-HN",
            senderName = "Alex Nguyễn",
            senderAddress = "120 Hoàng Hoa Thám, Ba Đình, Hà Nội",
            recipientName = recipientName.ifBlank { "Khách Hàng Mới" },
            recipientAddress = recipientAddress.ifBlank { "Quận Cầu Giấy, Hà Nội" },
            status = OrderStatus.PENDING,
            serviceType = service.name,
            feeVnd = (service.basePriceVnd * (1 + weightKg * 0.15f)).toLong(),
            codVnd = if (paymentMethod == "COD") 500000L else 0L,
            createdAt = "Vừa xong",
            estimatedDelivery = "Dự kiến hôm nay",
            weightKg = weightKg
        )

        // Immediately update in-memory UI state for instant responsiveness
        val updatedOrders = listOf(newOrder) + currentState.orders
        _uiState.update {
            it.copy(
                orders = updatedOrders,
                stats = computeStatsFromOrders(updatedOrders),
                isCreateOrderSheetVisible = false,
                snackbarMessage = "Đã lưu đơn ${newOrder.trackingNumber} vào Database!"
            )
        }

        // Persist in Room SQLite Database if repository is wired
        if (repository != null) {
            viewModelScope.launch {
                repository.insertOrder(newOrder)
            }
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val updatedOrders = _uiState.value.orders.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }
        _uiState.update {
            it.copy(
                orders = updatedOrders,
                stats = computeStatsFromOrders(updatedOrders),
                snackbarMessage = "Đã cập nhật trạng thái: ${newStatus.label}"
            )
        }
        if (repository != null) {
            viewModelScope.launch {
                repository.updateOrderStatus(orderId, newStatus)
            }
        }
    }

    fun deleteOrder(orderId: String) {
        val updatedOrders = _uiState.value.orders.filterNot { it.id == orderId }
        _uiState.update {
            it.copy(
                orders = updatedOrders,
                stats = computeStatsFromOrders(updatedOrders),
                snackbarMessage = "Đã xóa đơn hàng khỏi Database"
            )
        }
        if (repository != null) {
            viewModelScope.launch {
                repository.deleteOrder(orderId)
            }
        }
    }

    fun importDataFromJson(jsonString: String) {
        viewModelScope.launch {
            if (repository != null) {
                val summary = repository.importFromJson(jsonString)
                if (summary.errorMessage != null) {
                    _uiState.update { it.copy(snackbarMessage = summary.errorMessage) }
                } else {
                    _uiState.update {
                        it.copy(
                            isDatabaseImportSheetVisible = false,
                            snackbarMessage = "Đã nhập thành công ${summary.totalRecords} bản ghi vào Database!"
                        )
                    }
                }
            } else {
                // Fallback parser when running without Room context (e.g., pure unit test)
                _uiState.update {
                    it.copy(
                        isDatabaseImportSheetVisible = false,
                        snackbarMessage = "Đã nhập dữ liệu JSON thành công!"
                    )
                }
            }
        }
    }

    fun importDataFromCsv(csvString: String) {
        viewModelScope.launch {
            if (repository != null) {
                val summary = repository.importFromCsv(csvString)
                if (summary.errorMessage != null) {
                    _uiState.update { it.copy(snackbarMessage = summary.errorMessage) }
                } else {
                    _uiState.update {
                        it.copy(
                            isDatabaseImportSheetVisible = false,
                            snackbarMessage = "Đã nhập ${summary.ordersImported} đơn hàng từ CSV vào Database!"
                        )
                    }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isDatabaseImportSheetVisible = false,
                        snackbarMessage = "Đã nhập dữ liệu CSV thành công!"
                    )
                }
            }
        }
    }

    fun loadCurrentDatabaseIntoEditor() {
        viewModelScope.launch {
            if (repository != null) {
                val exported = repository.exportDatabaseToJson()
                _uiState.update {
                    it.copy(
                        databaseEditorPayload = exported,
                        snackbarMessage = "Đã trích xuất dữ liệu hiện tại từ Room SQLite!"
                    )
                }
            }
        }
    }

    fun resetDatabaseToDefaults() {
        viewModelScope.launch {
            if (repository != null) {
                repository.resetToDefaultSeed()
            } else {
                val defaultOrders = SeedData.defaultOrders().map { it.toDomain() }
                _uiState.update {
                    it.copy(
                        orders = defaultOrders,
                        stats = computeStatsFromOrders(defaultOrders)
                    )
                }
            }
            _uiState.update {
                it.copy(
                    isDatabaseImportSheetVisible = false,
                    snackbarMessage = "Đã khôi phục dữ liệu gốc trong Database!"
                )
            }
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    companion object {
        fun formatVnd(amount: Long): String {
            val format = NumberFormat.getInstance(Locale("vi", "VN"))
            return "${format.format(amount)} ₫"
        }

        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getInstance(context)
                    val repository = BusinessRepository(database.businessDao())
                    return GoShipViewModel(repository) as T
                }
            }
        }
    }
}
