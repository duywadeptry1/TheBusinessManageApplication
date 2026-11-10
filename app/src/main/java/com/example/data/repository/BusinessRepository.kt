package com.example.data.repository

import com.example.data.local.SeedData
import com.example.data.local.dao.BusinessDao
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.PaymentShareEntity
import com.example.data.local.entity.RevenueEntity
import com.example.data.local.entity.ServiceEntity
import com.example.data.local.entity.toEntity
import com.example.model.DailyRevenue
import com.example.model.OrderStatus
import com.example.model.PaymentMethodShare
import com.example.model.ShippingOrder
import com.example.model.ShippingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

data class ImportSummary(
    val ordersImported: Int = 0,
    val servicesImported: Int = 0,
    val revenuesImported: Int = 0,
    val paymentSharesImported: Int = 0,
    val errorMessage: String? = null
) {
    val totalRecords: Int
        get() = ordersImported + servicesImported + revenuesImported + paymentSharesImported
}

class BusinessRepository(private val dao: BusinessDao) {

    val ordersFlow: Flow<List<ShippingOrder>> = dao.getAllOrders().map { list ->
        list.map { it.toDomain() }
    }

    val servicesFlow: Flow<List<ShippingService>> = dao.getAllServices().map { list ->
        list.map { it.toDomain() }
    }

    val revenuesFlow: Flow<List<DailyRevenue>> = dao.getAllRevenues().map { list ->
        list.map { it.toDomain() }
    }

    val paymentSharesFlow: Flow<List<PaymentMethodShare>> = dao.getAllPaymentShares().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        if (dao.getOrdersCount() == 0) {
            dao.insertOrders(SeedData.defaultOrders())
        }
        if (dao.getServicesCount() == 0) {
            dao.insertServices(SeedData.defaultServices())
            dao.insertRevenues(SeedData.defaultRevenues())
            dao.insertPaymentShares(SeedData.defaultPaymentShares())
        }
    }

    suspend fun insertOrder(order: ShippingOrder) = withContext(Dispatchers.IO) {
        dao.insertOrder(order.toEntity(System.currentTimeMillis()))
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) = withContext(Dispatchers.IO) {
        dao.updateOrderStatus(orderId, status.name)
    }

    suspend fun deleteOrder(orderId: String) = withContext(Dispatchers.IO) {
        dao.deleteOrderById(orderId)
    }

    suspend fun resetToDefaultSeed() = withContext(Dispatchers.IO) {
        dao.clearAllOrders()
        dao.clearAllServices()
        dao.clearAllRevenues()
        dao.clearAllPaymentShares()

        dao.insertOrders(SeedData.defaultOrders())
        dao.insertServices(SeedData.defaultServices())
        dao.insertRevenues(SeedData.defaultRevenues())
        dao.insertPaymentShares(SeedData.defaultPaymentShares())
    }

    /**
     * Imports new records from a JSON string into the Room SQLite database.
     * Supports either a root JSONObject with "orders", "services", "revenues", "paymentShares"
     * or a root JSONArray of order objects.
     */
    suspend fun importFromJson(jsonString: String): ImportSummary = withContext(Dispatchers.IO) {
        try {
            val trimmed = jsonString.trim()
            if (trimmed.isEmpty()) {
                return@withContext ImportSummary(errorMessage = "Dữ liệu JSON trống.")
            }

            var ordersCount = 0
            var servicesCount = 0
            var revenuesCount = 0
            var sharesCount = 0

            val now = System.currentTimeMillis()

            if (trimmed.startsWith("[")) {
                val arr = JSONArray(trimmed)
                val entities = parseOrdersJsonArray(arr, now)
                dao.insertOrders(entities)
                ordersCount = entities.size
            } else {
                val root = JSONObject(trimmed)

                if (root.has("orders")) {
                    val ordersArr = root.getJSONArray("orders")
                    val entities = parseOrdersJsonArray(ordersArr, now)
                    dao.insertOrders(entities)
                    ordersCount = entities.size
                }

                if (root.has("services")) {
                    val srvArr = root.getJSONArray("services")
                    val srvList = mutableListOf<ServiceEntity>()
                    for (i in 0 until srvArr.length()) {
                        val obj = srvArr.getJSONObject(i)
                        srvList.add(
                            ServiceEntity(
                                id = obj.optString("id", "SRV-IMP-${i + 1}"),
                                name = obj.optString("name", "Dịch vụ mới"),
                                description = obj.optString("description", "Mô tả dịch vụ vận chuyển"),
                                basePriceVnd = obj.optLong("basePriceVnd", 25000L),
                                deliveryTime = obj.optString("deliveryTime", "Trong ngày"),
                                popularTag = obj.optString("popularTag").takeIf { it.isNotEmpty() }
                            )
                        )
                    }
                    dao.insertServices(srvList)
                    servicesCount = srvList.size
                }

                if (root.has("revenues")) {
                    val revArr = root.getJSONArray("revenues")
                    val revList = mutableListOf<RevenueEntity>()
                    for (i in 0 until revArr.length()) {
                        val obj = revArr.getJSONObject(i)
                        revList.add(
                            RevenueEntity(
                                dayIndex = obj.optInt("dayIndex", i),
                                dayLabel = obj.optString("dayLabel", "T${i + 2}"),
                                amountVnd = obj.optLong("amountVnd", 3000000L),
                                orderCount = obj.optInt("orderCount", 15),
                                isPeak = obj.optBoolean("isPeak", false)
                            )
                        )
                    }
                    dao.insertRevenues(revList)
                    revenuesCount = revList.size
                }

                if (root.has("paymentShares")) {
                    val shareArr = root.getJSONArray("paymentShares")
                    val shareList = mutableListOf<PaymentShareEntity>()
                    for (i in 0 until shareArr.length()) {
                        val obj = shareArr.getJSONObject(i)
                        shareList.add(
                            PaymentShareEntity(
                                name = obj.optString("name", "Phương thức ${i + 1}"),
                                percentage = obj.optInt("percentage", 30),
                                totalVnd = obj.optLong("totalVnd", 5000000L),
                                sortOrder = i
                            )
                        )
                    }
                    dao.insertPaymentShares(shareList)
                    sharesCount = shareList.size
                }
            }

            ImportSummary(
                ordersImported = ordersCount,
                servicesImported = servicesCount,
                revenuesImported = revenuesCount,
                paymentSharesImported = sharesCount
            )
        } catch (e: Exception) {
            ImportSummary(errorMessage = "Lỗi định dạng JSON: ${e.localizedMessage ?: "Không hợp lệ"}")
        }
    }

    private fun parseOrdersJsonArray(arr: JSONArray, baseTimestamp: Long): List<OrderEntity> {
        val list = mutableListOf<OrderEntity>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            val tracking = obj.optString("trackingNumber", "VN-${(10000..99999).random()}-DB")
            val id = obj.optString("id", "ORD-$tracking")
            val statusStr = obj.optString("status", OrderStatus.PENDING.name).uppercase()
            val validStatus = runCatching { OrderStatus.valueOf(statusStr).name }
                .getOrDefault(OrderStatus.PENDING.name)

            list.add(
                OrderEntity(
                    id = id,
                    trackingNumber = tracking,
                    senderName = obj.optString("senderName", "Alex Nguyễn"),
                    senderAddress = obj.optString("senderAddress", "120 Hoàng Hoa Thám, Ba Đình, Hà Nội"),
                    recipientName = obj.optString("recipientName", "Khách hàng GoShip"),
                    recipientAddress = obj.optString("recipientAddress", "Quận 1, TP. Hồ Chí Minh"),
                    statusName = validStatus,
                    serviceType = obj.optString("serviceType", "Hỏa tốc 2H"),
                    feeVnd = obj.optLong("feeVnd", 45000L),
                    codVnd = obj.optLong("codVnd", 500000L),
                    createdAt = obj.optString("createdAt", "Nhập từ Database"),
                    estimatedDelivery = obj.optString("estimatedDelivery", "Dự kiến hôm nay"),
                    weightKg = obj.optDouble("weightKg", 1.5).toFloat(),
                    timestampMillis = baseTimestamp + (arr.length() - i)
                )
            )
        }
        return list
    }

    /**
     * Imports orders from CSV lines into the Room SQLite database.
     * Expected columns: trackingNumber,senderName,senderAddress,recipientName,recipientAddress,status,serviceType,feeVnd,codVnd,weightKg
     */
    suspend fun importFromCsv(csvString: String): ImportSummary = withContext(Dispatchers.IO) {
        try {
            val lines = csvString.lines().map { it.trim() }.filter { it.isNotEmpty() }
            if (lines.isEmpty()) {
                return@withContext ImportSummary(errorMessage = "Dữ liệu CSV trống.")
            }

            val dataLines = if (lines.first().lowercase().startsWith("trackingnumber")) {
                lines.drop(1)
            } else {
                lines
            }

            val entities = mutableListOf<OrderEntity>()
            val now = System.currentTimeMillis()

            dataLines.forEachIndexed { index, line ->
                val cols = line.split(",").map { it.trim() }
                if (cols.size >= 5) {
                    val tracking = cols.getOrNull(0)?.ifEmpty { "VN-${(10000..99999).random()}-CSV" } ?: "VN-CSV"
                    val senderName = cols.getOrNull(1) ?: "Alex Nguyễn"
                    val senderAddr = cols.getOrNull(2) ?: "Hà Nội"
                    val recName = cols.getOrNull(3) ?: "Khách hàng"
                    val recAddr = cols.getOrNull(4) ?: "TP. Hồ Chí Minh"
                    val statusRaw = cols.getOrNull(5)?.uppercase() ?: OrderStatus.PENDING.name
                    val validStatus = runCatching { OrderStatus.valueOf(statusRaw).name }
                        .getOrDefault(OrderStatus.PENDING.name)
                    val service = cols.getOrNull(6) ?: "Tiết kiệm liên tỉnh"
                    val fee = cols.getOrNull(7)?.toLongOrNull() ?: 35000L
                    val cod = cols.getOrNull(8)?.toLongOrNull() ?: 0L
                    val weight = cols.getOrNull(9)?.toFloatOrNull() ?: 1.0f

                    entities.add(
                        OrderEntity(
                            id = "ORD-CSV-$tracking",
                            trackingNumber = tracking,
                            senderName = senderName,
                            senderAddress = senderAddr,
                            recipientName = recName,
                            recipientAddress = recAddr,
                            statusName = validStatus,
                            serviceType = service,
                            feeVnd = fee,
                            codVnd = cod,
                            createdAt = "Nhập từ CSV",
                            estimatedDelivery = "Dự kiến trong ngày",
                            weightKg = weight,
                            timestampMillis = now + (dataLines.size - index)
                        )
                    )
                }
            }

            if (entities.isEmpty()) {
                return@withContext ImportSummary(errorMessage = "Không tìm thấy dòng CSV hợp lệ (cần tối thiểu 5 cột).")
            }

            dao.insertOrders(entities)
            ImportSummary(ordersImported = entities.size)
        } catch (e: Exception) {
            ImportSummary(errorMessage = "Lỗi đọc CSV: ${e.localizedMessage ?: "Không hợp lệ"}")
        }
    }

    /**
     * Exports all current Room SQLite records into a formatted JSON string.
     */
    suspend fun exportDatabaseToJson(): String = withContext(Dispatchers.IO) {
        val root = JSONObject()
        val ordersArr = JSONArray()
        dao.getOrdersSnapshot().forEach { o ->
            val obj = JSONObject().apply {
                put("id", o.id)
                put("trackingNumber", o.trackingNumber)
                put("senderName", o.senderName)
                put("senderAddress", o.senderAddress)
                put("recipientName", o.recipientName)
                put("recipientAddress", o.recipientAddress)
                put("status", o.statusName)
                put("serviceType", o.serviceType)
                put("feeVnd", o.feeVnd)
                put("codVnd", o.codVnd)
                put("createdAt", o.createdAt)
                put("estimatedDelivery", o.estimatedDelivery)
                put("weightKg", o.weightKg.toDouble())
            }
            ordersArr.put(obj)
        }
        root.put("orders", ordersArr)

        val revArr = JSONArray()
        dao.getRevenuesSnapshot().forEach { r ->
            val obj = JSONObject().apply {
                put("dayIndex", r.dayIndex)
                put("dayLabel", r.dayLabel)
                put("amountVnd", r.amountVnd)
                put("orderCount", r.orderCount)
                put("isPeak", r.isPeak)
            }
            revArr.put(obj)
        }
        root.put("revenues", revArr)

        root.toString(2)
    }
}
