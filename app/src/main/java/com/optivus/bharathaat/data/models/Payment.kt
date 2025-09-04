package com.optivus.bharathaat.data.models

import com.google.firebase.firestore.PropertyName

/**
 * Payment data model for Firestore
 */
data class Payment(
    @get:PropertyName("payment_id") @set:PropertyName("payment_id")
    var paymentId: String = "",
    @get:PropertyName("order_id") @set:PropertyName("order_id")
    var orderId: String = "",
    @get:PropertyName("user_id") @set:PropertyName("user_id")
    var userId: String = "",
    @get:PropertyName("seller_id") @set:PropertyName("seller_id")
    var sellerId: String = "",
    val amount: Double = 0.0,
    val currency: String = "INR",
    @get:PropertyName("payment_method") @set:PropertyName("payment_method")
    var paymentMethod: String = "", // card, upi, netbanking, wallet, cod
    @get:PropertyName("payment_gateway") @set:PropertyName("payment_gateway")
    var paymentGateway: String = "", // razorpay, stripe, paytm, etc.
    @get:PropertyName("gateway_transaction_id") @set:PropertyName("gateway_transaction_id")
    var gatewayTransactionId: String? = null,
    @get:PropertyName("gateway_payment_id") @set:PropertyName("gateway_payment_id")
    var gatewayPaymentId: String? = null,
    val status: String = "pending", // pending, processing, completed, failed, cancelled, refunded
    @get:PropertyName("failure_reason") @set:PropertyName("failure_reason")
    var failureReason: String? = null,
    @get:PropertyName("refund_amount") @set:PropertyName("refund_amount")
    var refundAmount: Double = 0.0,
    @get:PropertyName("refund_reason") @set:PropertyName("refund_reason")
    var refundReason: String? = null,
    @get:PropertyName("refund_id") @set:PropertyName("refund_id")
    var refundId: String? = null,
    @get:PropertyName("commission_amount") @set:PropertyName("commission_amount")
    var commissionAmount: Double = 0.0,
    @get:PropertyName("seller_amount") @set:PropertyName("seller_amount")
    var sellerAmount: Double = 0.0,
    @get:PropertyName("settlement_status") @set:PropertyName("settlement_status")
    var settlementStatus: String = "pending", // pending, settled, failed
    @get:PropertyName("settlement_date") @set:PropertyName("settlement_date")
    var settlementDate: Long? = null,
    @get:PropertyName("payment_details") @set:PropertyName("payment_details")
    var paymentDetails: Map<String, Any> = emptyMap(), // Store gateway-specific details
    @get:PropertyName("created_at") @set:PropertyName("created_at")
    var createdAt: Long = 0L,
    @get:PropertyName("updated_at") @set:PropertyName("updated_at")
    var updatedAt: Long = 0L
)
