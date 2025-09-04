package com.optivus.bharathaat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.optivus.bharathaat.data.local.dao.OrderDao
import com.optivus.bharathaat.data.local.entities.toEntity
import com.optivus.bharathaat.data.local.entities.toOrder
import com.optivus.bharathaat.data.models.Order
import com.optivus.bharathaat.data.models.OrderStatusUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val orderDao: OrderDao
) {

    /**
     * Get orders for current buyer with offline support
     */
    fun getBuyerOrders(): Flow<List<Order>> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            try {
                // Try Firestore first
                val firestoreOrders = firestore.collection("orders")
                    .whereEqualTo("buyer_id", currentUser.uid)
                    .orderBy("created_at", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(Order::class.java) }

                // Cache locally
                orderDao.insertOrders(firestoreOrders.map { it.toEntity() })
                emit(firestoreOrders)
            } catch (e: Exception) {
                // Fallback to local cache
                val cachedOrders = orderDao.getOrdersByBuyer(currentUser.uid)
                emit(cachedOrders.map { it.toOrder() })
            }
        } else {
            emit(emptyList())
        }
    }

    /**
     * Get orders for current seller with offline support
     */
    fun getSellerOrders(): Flow<List<Order>> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            try {
                // Try Firestore first
                val firestoreOrders = firestore.collection("orders")
                    .whereEqualTo("seller_id", currentUser.uid)
                    .orderBy("created_at", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(Order::class.java) }

                // Cache locally
                orderDao.insertOrders(firestoreOrders.map { it.toEntity() })
                emit(firestoreOrders)
            } catch (e: Exception) {
                // Fallback to local cache
                val cachedOrders = orderDao.getOrdersBySeller(currentUser.uid)
                emit(cachedOrders.map { it.toOrder() })
            }
        } else {
            emit(emptyList())
        }
    }

    /**
     * Create new order (buyer)
     */
    suspend fun createOrder(order: Order): Result<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val orderId = UUID.randomUUID().toString()
                val newOrder = order.copy(
                    orderId = orderId,
                    buyerId = currentUser.uid,
                    status = "pending",
                    paymentStatus = "pending",
                    statusHistory = listOf(
                        OrderStatusUpdate(
                            status = "pending",
                            timestamp = System.currentTimeMillis(),
                            notes = "Order created",
                            updatedBy = currentUser.uid
                        )
                    ),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                // Save to Firestore
                firestore.collection("orders")
                    .document(orderId)
                    .set(newOrder)
                    .await()

                // Cache locally
                orderDao.insertOrder(newOrder.toEntity())

                Result.success(orderId)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update order status (seller)
     */
    suspend fun updateOrderStatus(
        orderId: String,
        newStatus: String,
        notes: String? = null,
        trackingNumber: String? = null
    ): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                // Get current order
                val orderDoc = firestore.collection("orders")
                    .document(orderId)
                    .get()
                    .await()

                val currentOrder = orderDoc.toObject(Order::class.java)
                if (currentOrder?.sellerId == currentUser.uid) {
                    val statusUpdate = OrderStatusUpdate(
                        status = newStatus,
                        timestamp = System.currentTimeMillis(),
                        notes = notes,
                        updatedBy = currentUser.uid
                    )

                    val updatedHistory = currentOrder.statusHistory + statusUpdate

                    val updates = mutableMapOf<String, Any>(
                        "status" to newStatus,
                        "status_history" to updatedHistory,
                        "updated_at" to System.currentTimeMillis()
                    )

                    trackingNumber?.let { updates["tracking_number"] = it }

                    // Update in Firestore
                    firestore.collection("orders")
                        .document(orderId)
                        .update(updates)
                        .await()

                    // Update local cache
                    val updatedOrder = currentOrder.copy(
                        status = newStatus,
                        trackingNumber = trackingNumber ?: currentOrder.trackingNumber,
                        statusHistory = updatedHistory,
                        updatedAt = System.currentTimeMillis()
                    )
                    orderDao.updateOrder(updatedOrder.toEntity())

                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Unauthorized"))
                }
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cancel order
     */
    suspend fun cancelOrder(orderId: String, reason: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val orderDoc = firestore.collection("orders")
                    .document(orderId)
                    .get()
                    .await()

                val currentOrder = orderDoc.toObject(Order::class.java)
                if (currentOrder?.buyerId == currentUser.uid || currentOrder?.sellerId == currentUser.uid) {
                    val statusUpdate = OrderStatusUpdate(
                        status = "cancelled",
                        timestamp = System.currentTimeMillis(),
                        notes = "Order cancelled: $reason",
                        updatedBy = currentUser.uid
                    )

                    val updatedHistory = currentOrder.statusHistory + statusUpdate

                    val updates = mapOf(
                        "status" to "cancelled",
                        "cancellation_reason" to reason,
                        "status_history" to updatedHistory,
                        "updated_at" to System.currentTimeMillis()
                    )

                    // Update in Firestore
                    firestore.collection("orders")
                        .document(orderId)
                        .update(updates)
                        .await()

                    // Update local cache
                    val updatedOrder = currentOrder.copy(
                        status = "cancelled",
                        cancellationReason = reason,
                        statusHistory = updatedHistory,
                        updatedAt = System.currentTimeMillis()
                    )
                    orderDao.updateOrder(updatedOrder.toEntity())

                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Unauthorized"))
                }
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get order by ID
     */
    suspend fun getOrderById(orderId: String): Result<Order?> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                try {
                    // Try Firestore first
                    val order = firestore.collection("orders")
                        .document(orderId)
                        .get()
                        .await()
                        .toObject(Order::class.java)

                    // Verify user has permission to view this order
                    if (order?.buyerId == currentUser.uid || order?.sellerId == currentUser.uid) {
                        order?.let { orderDao.insertOrder(it.toEntity()) }
                        Result.success(order)
                    } else {
                        Result.failure(Exception("Unauthorized"))
                    }
                } catch (e: Exception) {
                    // Fallback to local cache
                    val cachedOrder = orderDao.getOrderById(orderId)
                    Result.success(cachedOrder?.toOrder())
                }
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get orders by status for seller
     */
    suspend fun getSellerOrdersByStatus(status: String): Result<List<Order>> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val orders = firestore.collection("orders")
                    .whereEqualTo("seller_id", currentUser.uid)
                    .whereEqualTo("status", status)
                    .orderBy("created_at", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(Order::class.java) }

                Result.success(orders)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
