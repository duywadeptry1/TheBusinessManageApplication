package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.DailyRevenue
import com.example.model.OrderStatus
import com.example.model.PaymentMethodShare
import com.example.model.ShippingOrder
import com.example.model.ShippingService

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val trackingNumber: String,
    val senderName: String,
    val senderAddress: String,
    val recipientName: String,
    val recipientAddress: String,
    val statusName: String,
    val serviceType: String,
    val feeVnd: Long,
    val codVnd: Long,
    val createdAt: String,
    val estimatedDelivery: String,
    val weightKg: Float,
    val timestampMillis: Long = System.currentTimeMillis()
) {
    fun toDomain(): ShippingOrder {
        val status = runCatching { OrderStatus.valueOf(statusName.uppercase()) }
            .getOrDefault(OrderStatus.PENDING)
        return ShippingOrder(
            id = id,
            trackingNumber = trackingNumber,
            senderName = senderName,
            senderAddress = senderAddress,
            recipientName = recipientName,
            recipientAddress = recipientAddress,
            status = status,
            serviceType = serviceType,
            feeVnd = feeVnd,
            codVnd = codVnd,
            createdAt = createdAt,
            estimatedDelivery = estimatedDelivery,
            weightKg = weightKg
        )
    }
}

fun ShippingOrder.toEntity(timestampMillis: Long = System.currentTimeMillis()): OrderEntity {
    return OrderEntity(
        id = id,
        trackingNumber = trackingNumber,
        senderName = senderName,
        senderAddress = senderAddress,
        recipientName = recipientName,
        recipientAddress = recipientAddress,
        statusName = status.name,
        serviceType = serviceType,
        feeVnd = feeVnd,
        codVnd = codVnd,
        createdAt = createdAt,
        estimatedDelivery = estimatedDelivery,
        weightKg = weightKg,
        timestampMillis = timestampMillis
    )
}

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val basePriceVnd: Long,
    val deliveryTime: String,
    val popularTag: String? = null
) {
    fun toDomain(): ShippingService {
        return ShippingService(
            id = id,
            name = name,
            description = description,
            basePriceVnd = basePriceVnd,
            deliveryTime = deliveryTime,
            popularTag = popularTag
        )
    }
}

fun ShippingService.toEntity(): ServiceEntity {
    return ServiceEntity(
        id = id,
        name = name,
        description = description,
        basePriceVnd = basePriceVnd,
        deliveryTime = deliveryTime,
        popularTag = popularTag
    )
}

@Entity(tableName = "daily_revenues")
data class RevenueEntity(
    @PrimaryKey val dayIndex: Int,
    val dayLabel: String,
    val amountVnd: Long,
    val orderCount: Int,
    val isPeak: Boolean = false
) {
    fun toDomain(): DailyRevenue {
        return DailyRevenue(
            dayLabel = dayLabel,
            amountVnd = amountVnd,
            orderCount = orderCount,
            isPeak = isPeak
        )
    }
}

@Entity(tableName = "payment_shares")
data class PaymentShareEntity(
    @PrimaryKey val name: String,
    val percentage: Int,
    val totalVnd: Long,
    val sortOrder: Int = 0
) {
    fun toDomain(): PaymentMethodShare {
        return PaymentMethodShare(
            name = name,
            percentage = percentage,
            totalVnd = totalVnd
        )
    }
}
