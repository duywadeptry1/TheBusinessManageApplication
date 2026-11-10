package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.PaymentShareEntity
import com.example.data.local.entity.RevenueEntity
import com.example.data.local.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {

    // --- ORDERS ---
    @Query("SELECT * FROM orders ORDER BY timestampMillis DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders ORDER BY timestampMillis DESC")
    suspend fun getOrdersSnapshot(): List<OrderEntity>

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrdersCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Query("UPDATE orders SET statusName = :statusName WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, statusName: String)

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrderById(orderId: String)

    @Query("DELETE FROM orders")
    suspend fun clearAllOrders()

    // --- SERVICES ---
    @Query("SELECT * FROM services ORDER BY id ASC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services ORDER BY id ASC")
    suspend fun getServicesSnapshot(): List<ServiceEntity>

    @Query("SELECT COUNT(*) FROM services")
    suspend fun getServicesCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Query("DELETE FROM services")
    suspend fun clearAllServices()

    // --- DAILY REVENUES ---
    @Query("SELECT * FROM daily_revenues ORDER BY dayIndex ASC")
    fun getAllRevenues(): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM daily_revenues ORDER BY dayIndex ASC")
    suspend fun getRevenuesSnapshot(): List<RevenueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevenues(revenues: List<RevenueEntity>)

    @Query("DELETE FROM daily_revenues")
    suspend fun clearAllRevenues()

    // --- PAYMENT SHARES ---
    @Query("SELECT * FROM payment_shares ORDER BY sortOrder ASC")
    fun getAllPaymentShares(): Flow<List<PaymentShareEntity>>

    @Query("SELECT * FROM payment_shares ORDER BY sortOrder ASC")
    suspend fun getPaymentSharesSnapshot(): List<PaymentShareEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentShares(shares: List<PaymentShareEntity>)

    @Query("DELETE FROM payment_shares")
    suspend fun clearAllPaymentShares()
}
