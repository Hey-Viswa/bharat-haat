package com.optivus.bharathaat.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.optivus.bharathaat.data.models.Order
import com.optivus.bharathaat.data.models.Address
import com.optivus.bharathaat.data.models.OrderStatusUpdate

@Entity(tableName = "orders")
@TypeConverters(OrderEntityConverters::class)
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val buyerId: String,
    val buyerName: String,
    val sellerId: String,
    val sellerName: String,
    val productId: String,
    val productName: String,
    val productImageUrl: String?,
    val quantity: Int,
    val unitPrice: Double,
    val totalAmount: Double,
    val shippingFee: Double,
    val taxAmount: Double,
    val discountAmount: Double,
    val finalAmount: Double,
    val status: String,
    val shippingAddress: Address?,
    val trackingNumber: String?,
    val expectedDelivery: Long?,
    val deliveryInstructions: String?,
    val paymentMethod: String,
    val paymentStatus: String,
    val orderNotes: String?,
    val cancellationReason: String?,
    val statusHistory: List<OrderStatusUpdate>,
    val createdAt: Long,
    val updatedAt: Long,
    val lastSyncedAt: Long = System.currentTimeMillis()
)

class OrderEntityConverters {
    @TypeConverter
    fun fromAddress(address: Address?): String? {
        return address?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toAddress(addressString: String?): Address? {
        return addressString?.let {
            Gson().fromJson(it, Address::class.java)
        }
    }

    @TypeConverter
    fun fromStatusHistory(statusHistory: List<OrderStatusUpdate>): String {
        return Gson().toJson(statusHistory)
    }

    @TypeConverter
    fun toStatusHistory(statusHistoryString: String): List<OrderStatusUpdate> {
        return Gson().fromJson(statusHistoryString, object : TypeToken<List<OrderStatusUpdate>>() {}.type)
    }
}

// Extension functions for conversion
fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        orderId = orderId,
        buyerId = buyerId,
        buyerName = buyerName,
        sellerId = sellerId,
        sellerName = sellerName,
        productId = productId,
        productName = productName,
        productImageUrl = productImageUrl,
        quantity = quantity,
        unitPrice = unitPrice,
        totalAmount = totalAmount,
        shippingFee = shippingFee,
        taxAmount = taxAmount,
        discountAmount = discountAmount,
        finalAmount = finalAmount,
        status = status,
        shippingAddress = shippingAddress,
        trackingNumber = trackingNumber,
        expectedDelivery = expectedDelivery,
        deliveryInstructions = deliveryInstructions,
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus,
        orderNotes = orderNotes,
        cancellationReason = cancellationReason,
        statusHistory = statusHistory,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun OrderEntity.toOrder(): Order {
    return Order(
        orderId = orderId,
        buyerId = buyerId,
        buyerName = buyerName,
        sellerId = sellerId,
        sellerName = sellerName,
        productId = productId,
        productName = productName,
        productImageUrl = productImageUrl,
        quantity = quantity,
        unitPrice = unitPrice,
        totalAmount = totalAmount,
        shippingFee = shippingFee,
        taxAmount = taxAmount,
        discountAmount = discountAmount,
        finalAmount = finalAmount,
        status = status,
        shippingAddress = shippingAddress,
        trackingNumber = trackingNumber,
        expectedDelivery = expectedDelivery,
        deliveryInstructions = deliveryInstructions,
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus,
        orderNotes = orderNotes,
        cancellationReason = cancellationReason,
        statusHistory = statusHistory,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
