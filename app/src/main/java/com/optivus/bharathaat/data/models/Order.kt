package com.optivus.bharathaat.data.models

import com.google.firebase.firestore.PropertyName

/**
 * Order data model for Firestore
 */
data class Order(
    @get:PropertyName("order_id") @set:PropertyName("order_id")
    var orderId: String = "",
    @get:PropertyName("buyer_id") @set:PropertyName("buyer_id")
    var buyerId: String = "",
    @get:PropertyName("buyer_name") @set:PropertyName("buyer_name")
    var buyerName: String = "",
    @get:PropertyName("seller_id") @set:PropertyName("seller_id")
    var sellerId: String = "",
    @get:PropertyName("seller_name") @set:PropertyName("seller_name")
    var sellerName: String = "",
    @get:PropertyName("product_id") @set:PropertyName("product_id")
    var productId: String = "",
    @get:PropertyName("product_name") @set:PropertyName("product_name")
    var productName: String = "",
    @get:PropertyName("product_image_url") @set:PropertyName("product_image_url")
    var productImageUrl: String? = null,
    val quantity: Int = 1,
    @get:PropertyName("unit_price") @set:PropertyName("unit_price")
    var unitPrice: Double = 0.0,
    @get:PropertyName("total_amount") @set:PropertyName("total_amount")
    var totalAmount: Double = 0.0,
    @get:PropertyName("shipping_fee") @set:PropertyName("shipping_fee")
    var shippingFee: Double = 0.0,
    @get:PropertyName("tax_amount") @set:PropertyName("tax_amount")
    var taxAmount: Double = 0.0,
    @get:PropertyName("discount_amount") @set:PropertyName("discount_amount")
    var discountAmount: Double = 0.0,
    @get:PropertyName("final_amount") @set:PropertyName("final_amount")
    var finalAmount: Double = 0.0,
    val status: String = "pending", // pending, confirmed, shipped, delivered, cancelled, returned
    @get:PropertyName("shipping_address") @set:PropertyName("shipping_address")
    var shippingAddress: Address? = null,
    @get:PropertyName("tracking_number") @set:PropertyName("tracking_number")
    var trackingNumber: String? = null,
    @get:PropertyName("expected_delivery") @set:PropertyName("expected_delivery")
    var expectedDelivery: Long? = null,
    @get:PropertyName("delivery_instructions") @set:PropertyName("delivery_instructions")
    var deliveryInstructions: String? = null,
    @get:PropertyName("payment_method") @set:PropertyName("payment_method")
    var paymentMethod: String = "",
    @get:PropertyName("payment_status") @set:PropertyName("payment_status")
    var paymentStatus: String = "pending", // pending, paid, failed, refunded
    @get:PropertyName("order_notes") @set:PropertyName("order_notes")
    var orderNotes: String? = null,
    @get:PropertyName("cancellation_reason") @set:PropertyName("cancellation_reason")
    var cancellationReason: String? = null,
    @get:PropertyName("status_history") @set:PropertyName("status_history")
    var statusHistory: List<OrderStatusUpdate> = emptyList(),
    @get:PropertyName("created_at") @set:PropertyName("created_at")
    var createdAt: Long = 0L,
    @get:PropertyName("updated_at") @set:PropertyName("updated_at")
    var updatedAt: Long = 0L
)

/**
 * Address data model
 */
data class Address(
    val name: String = "",
    @get:PropertyName("phone_number") @set:PropertyName("phone_number")
    var phoneNumber: String = "",
    @get:PropertyName("address_line1") @set:PropertyName("address_line1")
    var addressLine1: String = "",
    @get:PropertyName("address_line2") @set:PropertyName("address_line2")
    var addressLine2: String? = null,
    val city: String = "",
    val state: String = "",
    val pincode: String = "",
    val landmark: String? = null,
    @get:PropertyName("address_type") @set:PropertyName("address_type")
    var addressType: String = "home" // home, work, other
)

/**
 * Order status update for tracking history
 */
data class OrderStatusUpdate(
    val status: String = "",
    val timestamp: Long = 0L,
    val notes: String? = null,
    @get:PropertyName("updated_by") @set:PropertyName("updated_by")
    var updatedBy: String = "" // user_id who updated the status
)
