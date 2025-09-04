package com.optivus.bharathaat.data.local.dao

import androidx.room.*
import com.optivus.bharathaat.data.local.entities.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: String): OrderEntity?

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    fun getOrderByIdFlow(orderId: String): Flow<OrderEntity?>

    @Query("SELECT * FROM orders WHERE buyerId = :buyerId ORDER BY createdAt DESC")
    suspend fun getOrdersByBuyer(buyerId: String): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE buyerId = :buyerId ORDER BY createdAt DESC")
    fun getOrdersByBuyerFlow(buyerId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    suspend fun getOrdersBySeller(sellerId: String): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getOrdersBySellerFlow(sellerId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE sellerId = :sellerId AND status = :status ORDER BY createdAt DESC")
    suspend fun getOrdersBySellerAndStatus(sellerId: String, status: String): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE buyerId = :buyerId AND status = :status ORDER BY createdAt DESC")
    fun getOrdersByBuyerAndStatusFlow(buyerId: String, status: String): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Query("DELETE FROM orders WHERE orderId = :orderId")
    suspend fun deleteOrderById(orderId: String)

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
}
