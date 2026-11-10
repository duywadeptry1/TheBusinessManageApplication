package com.example.model

enum class OrderStatus(val label: String) {
    DELIVERING("Đang giao hàng"),
    PENDING("Chờ lấy hàng"),
    COMPLETED("Đã giao thành công"),
    CANCELLED("Đã hủy đơn")
}

data class ShippingOrder(
    val id: String,
    val trackingNumber: String,
    val senderName: String,
    val senderAddress: String,
    val recipientName: String,
    val recipientAddress: String,
    val status: OrderStatus,
    val serviceType: String,
    val feeVnd: Long,
    val codVnd: Long,
    val createdAt: String,
    val estimatedDelivery: String,
    val weightKg: Float
)

data class StatCardData(
    val title: String,
    val count: String,
    val subtitle: String,
    val trend: String,
    val isPositive: Boolean
)

data class ShippingService(
    val id: String,
    val name: String,
    val description: String,
    val basePriceVnd: Long,
    val deliveryTime: String,
    val popularTag: String? = null
)

data class DailyRevenue(
    val dayLabel: String,
    val amountVnd: Long,
    val orderCount: Int,
    val isPeak: Boolean = false
)

data class PaymentMethodShare(
    val name: String,
    val percentage: Int,
    val totalVnd: Long
)

data class CourierDriver(
    val name: String,
    val phone: String,
    val licensePlate: String,
    val vehicleModel: String,
    val rating: Float,
    val completedTrips: Int,
    val currentDistanceKm: Float,
    val etaMinutes: Int
)

data class TrackingStep(
    val title: String,
    val description: String,
    val timestamp: String,
    val isDone: Boolean,
    val isCurrent: Boolean
)
