package com.optivus.bharathaat.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.optivus.bharathaat.data.models.Product
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSeeder @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun seedProductData(): Result<String> {
        return try {
            val sampleProducts = getSampleProducts()
            
            sampleProducts.forEach { product ->
                firestore.collection("products")
                    .document(product.productId)
                    .set(product)
                    .await()
            }
            
            Result.success("Successfully seeded ${sampleProducts.size} products to Firestore")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getSampleProducts(): List<Product> {
        val currentTime = System.currentTimeMillis()
        
        return listOf(
            Product(
                productId = "prod_001",
                sellerId = "seller_001",
                sellerName = "Artisan Crafts",
                name = "Handwoven Banarasi Silk Saree",
                description = "Exquisite handwoven Banarasi silk saree with intricate zari work. This traditional piece showcases the rich heritage of Indian craftsmanship. Perfect for weddings and special occasions.",
                price = 8999.0,
                originalPrice = 12999.0,
                category = "Traditional Clothing",
                subcategory = "Sarees",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=500",
                    "https://images.unsplash.com/photo-1583391733956-6c78276477e2?w=500"
                ),
                mainImageUrl = "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=500",
                stock = 15,
                brand = "Heritage Silks",
                weight = "600g",
                isActive = true,
                isFeatured = true,
                tags = listOf("silk", "saree", "traditional", "handwoven", "banarasi"),
                specifications = mapOf(
                    "Material" to "Pure Silk",
                    "Work" to "Zari",
                    "Length" to "5.5 meters",
                    "Blouse" to "0.8 meters included"
                ),
                discountPercentage = 30.77,
                minOrderQuantity = 1,
                maxOrderQuantity = 5,
                returnPolicy = "15 days return policy",
                shippingWeight = 0.8,
                viewCount = 245,
                ratingAverage = 4.8,
                ratingCount = 32,
                createdAt = currentTime,
                updatedAt = currentTime
            ),
            
            Product(
                productId = "prod_002",
                sellerId = "seller_002",
                sellerName = "Heritage Metals",
                name = "Brass Kitchen Utensils Set",
                description = "Traditional brass kitchen utensils set including plates, bowls, and serving spoons. Made from pure brass with antimicrobial properties. Ideal for authentic Indian cooking.",
                price = 2499.0,
                originalPrice = 3499.0,
                category = "Kitchen & Cookware",
                subcategory = "Utensils",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=500",
                    "https://images.unsplash.com/photo-1584464491033-06628f3a6b7b?w=500"
                ),
                mainImageUrl = "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=500",
                stock = 25,
                brand = "Traditional Brass",
                weight = "2.5kg",
                isActive = true,
                isFeatured = true,
                tags = listOf("brass", "kitchen", "utensils", "traditional", "set"),
                specifications = mapOf(
                    "Material" to "Pure Brass",
                    "Items" to "12 pieces",
                    "Finish" to "Hand polished",
                    "Care" to "Hand wash only"
                ),
                discountPercentage = 28.58,
                minOrderQuantity = 1,
                maxOrderQuantity = 3,
                returnPolicy = "7 days return policy",
                shippingWeight = 3.0,
                viewCount = 189,
                ratingAverage = 4.5,
                ratingCount = 28,
                createdAt = currentTime,
                updatedAt = currentTime
            ),

            Product(
                productId = "prod_003",
                sellerId = "seller_003",
                sellerName = "Wood Works India",
                name = "Handcarved Sandalwood Ganesha",
                description = "Beautiful handcarved Ganesha statue made from pure sandalwood. This divine sculpture brings peace and prosperity to your home. Perfect for worship and home decoration.",
                price = 1899.0,
                originalPrice = 2599.0,
                category = "Handicrafts & Art",
                subcategory = "Religious Items",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=500",
                    "https://images.unsplash.com/photo-1605106715994-18d3fecffb98?w=500"
                ),
                mainImageUrl = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=500",
                stock = 8,
                brand = "Mysore Crafts",
                dimensions = "6 x 4 x 8 inches",
                isActive = true,
                isFeatured = false,
                tags = listOf("sandalwood", "ganesha", "handcarved", "religious", "statue"),
                specifications = mapOf(
                    "Material" to "Pure Sandalwood",
                    "Height" to "8 inches",
                    "Finish" to "Natural wood finish",
                    "Origin" to "Karnataka"
                ),
                discountPercentage = 26.93,
                minOrderQuantity = 1,
                maxOrderQuantity = 2,
                returnPolicy = "No returns on religious items",
                shippingWeight = 0.5,
                viewCount = 156,
                ratingAverage = 4.9,
                ratingCount = 18,
                createdAt = currentTime,
                updatedAt = currentTime
            ),

            Product(
                productId = "prod_004",
                sellerId = "seller_004",
                sellerName = "Farm Fresh Organics",
                name = "Organic Spice Collection",
                description = "Premium collection of 20 organic spices sourced directly from Indian farms. No chemicals, no preservatives. Enhance your cooking with authentic flavors.",
                price = 899.0,
                originalPrice = 1199.0,
                category = "Organic Food",
                subcategory = "Spices",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=500",
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=500"
                ),
                mainImageUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=500",
                stock = 50,
                brand = "Organic Harvest",
                weight = "1kg",
                isActive = true,
                isFeatured = true,
                tags = listOf("organic", "spices", "natural", "collection", "farm-fresh"),
                specifications = mapOf(
                    "Count" to "20 spices",
                    "Packaging" to "Airtight containers",
                    "Shelf Life" to "24 months",
                    "Certification" to "Organic certified"
                ),
                discountPercentage = 25.02,
                minOrderQuantity = 1,
                maxOrderQuantity = 10,
                returnPolicy = "No returns on food items",
                shippingWeight = 1.2,
                viewCount = 312,
                ratingAverage = 4.7,
                ratingCount = 45,
                createdAt = currentTime,
                updatedAt = currentTime
            ),

            Product(
                productId = "prod_005",
                sellerId = "seller_005",
                sellerName = "Traditional Wear",
                name = "Block Print Cotton Kurta",
                description = "Comfortable cotton kurta with traditional block print patterns. Made from pure cotton fabric with natural dyes. Perfect for casual and formal occasions.",
                price = 1299.0,
                originalPrice = 1899.0,
                category = "Traditional Clothing",
                subcategory = "Kurtas",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1594633313593-bab3825d0caf?w=500",
                    "https://images.unsplash.com/photo-1506629905270-11176ba9dcc4?w=500"
                ),
                mainImageUrl = "https://images.unsplash.com/photo-1594633313593-bab3825d0caf?w=500",
                stock = 35,
                brand = "Rajasthani Prints",
                isActive = true,
                isFeatured = false,
                tags = listOf("cotton", "kurta", "block-print", "traditional", "men"),
                specifications = mapOf(
                    "Material" to "100% Cotton",
                    "Print" to "Hand block print",
                    "Sizes" to "S, M, L, XL, XXL",
                    "Care" to "Machine washable"
                ),
                discountPercentage = 31.59,
                minOrderQuantity = 1,
                maxOrderQuantity = 5,
                returnPolicy = "10 days exchange policy",
                shippingWeight = 0.3,
                viewCount = 198,
                ratingAverage = 4.3,
                ratingCount = 22,
                createdAt = currentTime,
                updatedAt = currentTime
            ),

            Product(
                productId = "prod_006",
                sellerId = "seller_006",
                sellerName = "Clay Creations",
                name = "Handmade Terracotta Pottery Set",
                description = "Beautiful handmade terracotta pottery set including vases, bowls, and decorative items. Perfect for home decoration and plant pots. Eco-friendly and sustainable.",
                price = 1599.0,
                originalPrice = 2199.0,
                category = "Home Decor",
                subcategory = "Pottery",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?w=500",
                    "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=500"
                ),
                mainImageUrl = "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?w=500",
                stock = 12,
                brand = "Earth Pottery",
                weight = "3kg",
                isActive = true,
                isFeatured = true,
                tags = listOf("terracotta", "pottery", "handmade", "eco-friendly", "decor"),
                specifications = mapOf(
                    "Material" to "Natural terracotta clay",
                    "Items" to "8 pieces",
                    "Finish" to "Natural clay finish",
                    "Use" to "Indoor/outdoor decoration"
                ),
                discountPercentage = 27.28,
                minOrderQuantity = 1,
                maxOrderQuantity = 3,
                returnPolicy = "7 days return policy (fragile items)",
                shippingWeight = 4.0,
                viewCount = 167,
                ratingAverage = 4.6,
                ratingCount = 15,
                createdAt = currentTime,
                updatedAt = currentTime
            ),

            Product(
                productId = "prod_007",
                sellerId = "seller_007",
                sellerName = "Jewelry Palace",
                name = "Oxidized Silver Jhumka Earrings",
                description = "Traditional oxidized silver jhumka earrings with intricate patterns. Handcrafted by skilled artisans. Perfect for ethnic wear and festivals.",
                price = 799.0,
                originalPrice = 1099.0,
                category = "Jewelry & Accessories",
                subcategory = "Earrings",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=500",
                    "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=500"
                ),
                mainImageUrl = "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=500",
                stock = 28,
                brand = "Silver Craft",
                weight = "25g",
                isActive = true,
                isFeatured = false,
                tags = listOf("silver", "jhumka", "earrings", "traditional", "oxidized"),
                specifications = mapOf(
                    "Material" to "92.5% Silver",
                    "Finish" to "Oxidized",
                    "Weight" to "25 grams",
                    "Style" to "Traditional Jhumka"
                ),
                discountPercentage = 27.30,
                minOrderQuantity = 1,
                maxOrderQuantity = 3,
                returnPolicy = "Exchange only within 5 days",
                shippingWeight = 0.1,
                viewCount = 234,
                ratingAverage = 4.4,
                ratingCount = 31,
                createdAt = currentTime,
                updatedAt = currentTime
            ),

            Product(
                productId = "prod_008",
                sellerId = "seller_008",
                sellerName = "Textile Treasures",
                name = "Handloom Cotton Bed Sheet Set",
                description = "Premium handloom cotton bed sheet set with pillow covers. Made from 100% organic cotton with natural dyes. Soft, breathable, and durable.",
                price = 1799.0,
                originalPrice = 2499.0,
                category = "Textiles & Fabrics",
                subcategory = "Bed Sheets",
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=500",
                    "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=500"
                ),
                mainImageUrl = "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=500",
                stock = 20,
                brand = "Handloom Heritage",
                weight = "800g",
                isActive = true,
                isFeatured = true,
                tags = listOf("handloom", "cotton", "bedsheet", "organic", "textile"),
                specifications = mapOf(
                    "Material" to "100% Organic Cotton",
                    "Thread Count" to "300 TC",
                    "Size" to "Double bed",
                    "Items" to "1 bedsheet + 2 pillow covers"
                ),
                discountPercentage = 28.01,
                minOrderQuantity = 1,
                maxOrderQuantity = 4,
                returnPolicy = "15 days return policy",
                shippingWeight = 1.0,
                viewCount = 145,
                ratingAverage = 4.5,
                ratingCount = 19,
                createdAt = currentTime,
                updatedAt = currentTime
            )
        )
    }
}
