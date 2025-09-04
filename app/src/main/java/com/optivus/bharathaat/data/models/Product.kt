package com.optivus.bharathaat.data.models

import com.google.firebase.firestore.PropertyName

/**
 * Product data model for Firestore
 */
data class Product(
    @get:PropertyName("product_id") @set:PropertyName("product_id")
    var productId: String = "",
    @get:PropertyName("seller_id") @set:PropertyName("seller_id")
    var sellerId: String = "",
    @get:PropertyName("seller_name") @set:PropertyName("seller_name")
    var sellerName: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val subcategory: String? = null,
    @get:PropertyName("image_urls") @set:PropertyName("image_urls")
    var imageUrls: List<String> = emptyList(),
    @get:PropertyName("main_image_url") @set:PropertyName("main_image_url")
    var mainImageUrl: String? = null,
    val stock: Int = 0,
    val brand: String? = null,
    val weight: String? = null,
    val dimensions: String? = null,
    val sku: String? = null,
    @get:PropertyName("is_active") @set:PropertyName("is_active")
    var isActive: Boolean = true,
    @get:PropertyName("is_featured") @set:PropertyName("is_featured")
    var isFeatured: Boolean = false,
    val tags: List<String> = emptyList(),
    val specifications: Map<String, String> = emptyMap(),
    @get:PropertyName("discount_percentage") @set:PropertyName("discount_percentage")
    var discountPercentage: Double = 0.0,
    @get:PropertyName("original_price") @set:PropertyName("original_price")
    var originalPrice: Double = 0.0,
    @get:PropertyName("min_order_quantity") @set:PropertyName("min_order_quantity")
    var minOrderQuantity: Int = 1,
    @get:PropertyName("max_order_quantity") @set:PropertyName("max_order_quantity")
    var maxOrderQuantity: Int? = null,
    @get:PropertyName("return_policy") @set:PropertyName("return_policy")
    var returnPolicy: String? = null,
    @get:PropertyName("shipping_weight") @set:PropertyName("shipping_weight")
    var shippingWeight: Double? = null,
    @get:PropertyName("view_count") @set:PropertyName("view_count")
    var viewCount: Int = 0,
    @get:PropertyName("rating_average") @set:PropertyName("rating_average")
    var ratingAverage: Double = 0.0,
    @get:PropertyName("rating_count") @set:PropertyName("rating_count")
    var ratingCount: Int = 0,
    @get:PropertyName("created_at") @set:PropertyName("created_at")
    var createdAt: Long = 0L,
    @get:PropertyName("updated_at") @set:PropertyName("updated_at")
    var updatedAt: Long = 0L
)
