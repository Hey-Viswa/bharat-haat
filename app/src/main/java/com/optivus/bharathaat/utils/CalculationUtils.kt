package com.optivus.bharathaat.utils

import com.optivus.bharathaat.constants.AppConstants
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

object CalculationUtils {

    /**
     * Price calculations
     */
    fun calculateDiscountAmount(originalPrice: Double, discountPercentage: Double): Double {
        return if (discountPercentage <= 0) 0.0
        else (originalPrice * discountPercentage) / 100.0
    }

    fun calculateDiscountedPrice(originalPrice: Double, discountPercentage: Double): Double {
        val discountAmount = calculateDiscountAmount(originalPrice, discountPercentage)
        return originalPrice - discountAmount
    }

    fun calculateDiscountPercentage(originalPrice: Double, discountedPrice: Double): Double {
        return if (originalPrice <= 0) 0.0
        else ((originalPrice - discountedPrice) / originalPrice) * 100.0
    }

    fun calculateSavings(originalPrice: Double, discountedPrice: Double): Double {
        return if (originalPrice <= discountedPrice) 0.0
        else originalPrice - discountedPrice
    }

    /**
     * Tax calculations
     */
    fun calculateGST(amount: Double, gstPercentage: Double): Double {
        return (amount * gstPercentage) / 100.0
    }

    fun calculateAmountWithGST(amount: Double, gstPercentage: Double): Double {
        return amount + calculateGST(amount, gstPercentage)
    }

    fun calculateAmountWithoutGST(amountWithGST: Double, gstPercentage: Double): Double {
        return amountWithGST / (1 + (gstPercentage / 100.0))
    }

    fun calculateCGST(amount: Double, gstPercentage: Double): Double {
        return calculateGST(amount, gstPercentage / 2)
    }

    fun calculateSGST(amount: Double, gstPercentage: Double): Double {
        return calculateGST(amount, gstPercentage / 2)
    }

    fun calculateIGST(amount: Double, gstPercentage: Double): Double {
        return calculateGST(amount, gstPercentage)
    }

    /**
     * Cart calculations
     */
    fun calculateSubtotal(cartItems: List<CartItem>): Double {
        return cartItems.sumOf { it.price * it.quantity }
    }

    fun calculateTotalItems(cartItems: List<CartItem>): Int {
        return cartItems.sumOf { it.quantity }
    }

    fun calculateTotalDiscount(cartItems: List<CartItem>): Double {
        return cartItems.sumOf {
            calculateDiscountAmount(it.originalPrice * it.quantity, it.discountPercentage)
        }
    }

    fun calculateCartTotal(
        subtotal: Double,
        deliveryCharge: Double = 0.0,
        packagingCharge: Double = 0.0,
        couponDiscount: Double = 0.0,
        gstPercentage: Double = 0.0
    ): Double {
        val totalBeforeTax = subtotal + deliveryCharge + packagingCharge - couponDiscount
        val gstAmount = if (gstPercentage > 0) calculateGST(totalBeforeTax, gstPercentage) else 0.0
        return totalBeforeTax + gstAmount
    }

    /**
     * Delivery charge calculations
     */
    fun calculateDeliveryCharge(
        subtotal: Double,
        distance: Double = 0.0,
        weight: Double = 0.0,
        isCOD: Boolean = false
    ): Double {
        var deliveryCharge = 0.0

        // Free delivery above threshold
        if (subtotal >= AppConstants.FREE_DELIVERY_THRESHOLD) {
            deliveryCharge = 0.0
        } else {
            deliveryCharge = AppConstants.DEFAULT_DELIVERY_CHARGE

            // Additional charges based on distance (if applicable)
            if (distance > 10) {
                deliveryCharge += (distance - 10) * 2 // ₹2 per km beyond 10km
            }

            // Additional charges based on weight (if applicable)
            if (weight > 2) {
                deliveryCharge += (weight - 2) * 10 // ₹10 per kg beyond 2kg
            }
        }

        // COD charges
        if (isCOD) {
            deliveryCharge += AppConstants.COD_CHARGE
        }

        return deliveryCharge
    }

    /**
     * Coupon calculations
     */
    fun applyCoupon(
        subtotal: Double,
        couponType: CouponType,
        couponValue: Double,
        maxDiscount: Double = Double.MAX_VALUE,
        minOrderAmount: Double = 0.0
    ): CouponResult {
        if (subtotal < minOrderAmount) {
            return CouponResult(
                isValid = false,
                discountAmount = 0.0,
                message = "Minimum order amount is ₹$minOrderAmount"
            )
        }

        val discountAmount = when (couponType) {
            CouponType.PERCENTAGE -> {
                val discount = (subtotal * couponValue) / 100.0
                if (discount > maxDiscount) maxDiscount else discount
            }
            CouponType.FIXED_AMOUNT -> {
                if (couponValue > subtotal) subtotal else couponValue
            }
            CouponType.BUY_X_GET_Y -> {
                // Implementation depends on specific product logic
                0.0
            }
        }

        return CouponResult(
            isValid = true,
            discountAmount = discountAmount,
            message = "Coupon applied successfully"
        )
    }

    /**
     * EMI calculations
     */
    fun calculateEMI(principal: Double, ratePerMonth: Double, tenure: Int): Double {
        val r = ratePerMonth / 100.0
        return (principal * r * Math.pow(1 + r, tenure.toDouble())) /
               (Math.pow(1 + r, tenure.toDouble()) - 1)
    }

    fun calculateTotalInterest(emi: Double, tenure: Int, principal: Double): Double {
        return (emi * tenure) - principal
    }

    /**
     * Loyalty points calculations
     */
    fun calculateEarnedPoints(amount: Double, pointsPerRupee: Double = 1.0): Int {
        return floor(amount * pointsPerRupee).toInt()
    }

    fun calculatePointsValue(points: Int, valuePerPoint: Double = 1.0): Double {
        return points * valuePerPoint
    }

    /**
     * Rating calculations
     */
    fun calculateAverageRating(ratings: List<Int>): Double {
        return if (ratings.isEmpty()) 0.0
        else ratings.average()
    }

    fun calculateRatingDistribution(ratings: List<Int>): Map<Int, Double> {
        val total = ratings.size.toDouble()
        return (1..5).associateWith { star ->
            if (total == 0.0) 0.0
            else (ratings.count { it == star } / total) * 100.0
        }
    }

    /**
     * Profit margin calculations
     */
    fun calculateProfitMargin(sellingPrice: Double, costPrice: Double): Double {
        return if (sellingPrice <= 0) 0.0
        else ((sellingPrice - costPrice) / sellingPrice) * 100.0
    }

    fun calculateMarkup(sellingPrice: Double, costPrice: Double): Double {
        return if (costPrice <= 0) 0.0
        else ((sellingPrice - costPrice) / costPrice) * 100.0
    }

    /**
     * Currency formatting
     */
    fun formatCurrency(amount: Double, showSymbol: Boolean = true): String {
        val formattedAmount = String.format("%.${AppConstants.DEFAULT_DECIMAL_PLACES}f", amount)
        return if (showSymbol) "${AppConstants.CURRENCY_SYMBOL}$formattedAmount" else formattedAmount
    }

    fun formatCurrencyCompact(amount: Double): String {
        return when {
            amount >= 10_000_000 -> "${AppConstants.CURRENCY_SYMBOL}${String.format("%.1f", amount / 10_000_000)}Cr"
            amount >= 100_000 -> "${AppConstants.CURRENCY_SYMBOL}${String.format("%.1f", amount / 100_000)}L"
            amount >= 1_000 -> "${AppConstants.CURRENCY_SYMBOL}${String.format("%.1f", amount / 1_000)}K"
            else -> formatCurrency(amount)
        }
    }

    /**
     * Percentage calculations
     */
    fun formatPercentage(percentage: Double, decimalPlaces: Int = 1): String {
        return "${String.format("%.${decimalPlaces}f", percentage)}%"
    }

    fun roundToNearest(value: Double, nearest: Double = 1.0): Double {
        return round(value / nearest) * nearest
    }

    fun roundUpToNearest(value: Double, nearest: Double = 1.0): Double {
        return ceil(value / nearest) * nearest
    }

    fun roundDownToNearest(value: Double, nearest: Double = 1.0): Double {
        return floor(value / nearest) * nearest
    }
}

// Data classes for calculations
data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val originalPrice: Double,
    val quantity: Int,
    val discountPercentage: Double = 0.0,
    val gstPercentage: Double = 0.0
)

data class CouponResult(
    val isValid: Boolean,
    val discountAmount: Double,
    val message: String
)

enum class CouponType {
    PERCENTAGE,
    FIXED_AMOUNT,
    BUY_X_GET_Y
}
