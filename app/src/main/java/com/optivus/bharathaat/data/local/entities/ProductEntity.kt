package com.optivus.bharathaat.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.optivus.bharathaat.data.models.Product

@Entity(tableName = "products")
@TypeConverters(ProductEntityConverters::class)
data class ProductEntity(
    @PrimaryKey
    val productId: String,
    val sellerId: String,
    val sellerName: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val subcategory: String?,
    val imageUrls: List<String>,
    val mainImageUrl: String?,
    val stock: Int,
    val brand: String?,
    val weight: String?,
    val dimensions: String?,
    val sku: String?,
    val isActive: Boolean,
    val isFeatured: Boolean,
    val tags: List<String>,
    val specifications: Map<String, String>,
    val discountPercentage: Double,
    val originalPrice: Double,
    val minOrderQuantity: Int,
    val maxOrderQuantity: Int?,
    val returnPolicy: String?,
    val shippingWeight: Double?,
    val viewCount: Int,
    val ratingAverage: Double,
    val ratingCount: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val lastSyncedAt: Long = System.currentTimeMillis()
)

class ProductEntityConverters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun fromStringMap(value: Map<String, String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringMap(value: String): Map<String, String> {
        return Gson().fromJson(value, object : TypeToken<Map<String, String>>() {}.type)
    }
}

// Extension functions for conversion
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        productId = productId,
        sellerId = sellerId,
        sellerName = sellerName,
        name = name,
        description = description,
        price = price,
        category = category,
        subcategory = subcategory,
        imageUrls = imageUrls,
        mainImageUrl = mainImageUrl,
        stock = stock,
        brand = brand,
        weight = weight,
        dimensions = dimensions,
        sku = sku,
        isActive = isActive,
        isFeatured = isFeatured,
        tags = tags,
        specifications = specifications,
        discountPercentage = discountPercentage,
        originalPrice = originalPrice,
        minOrderQuantity = minOrderQuantity,
        maxOrderQuantity = maxOrderQuantity,
        returnPolicy = returnPolicy,
        shippingWeight = shippingWeight,
        viewCount = viewCount,
        ratingAverage = ratingAverage,
        ratingCount = ratingCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ProductEntity.toProduct(): Product {
    return Product(
        productId = productId,
        sellerId = sellerId,
        sellerName = sellerName,
        name = name,
        description = description,
        price = price,
        category = category,
        subcategory = subcategory,
        imageUrls = imageUrls,
        mainImageUrl = mainImageUrl,
        stock = stock,
        brand = brand,
        weight = weight,
        dimensions = dimensions,
        sku = sku,
        isActive = isActive,
        isFeatured = isFeatured,
        tags = tags,
        specifications = specifications,
        discountPercentage = discountPercentage,
        originalPrice = originalPrice,
        minOrderQuantity = minOrderQuantity,
        maxOrderQuantity = maxOrderQuantity,
        returnPolicy = returnPolicy,
        shippingWeight = shippingWeight,
        viewCount = viewCount,
        ratingAverage = ratingAverage,
        ratingCount = ratingCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
