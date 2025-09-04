package com.optivus.bharathaat.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.optivus.bharathaat.data.local.dao.ProductDao
import com.optivus.bharathaat.data.local.entities.toEntity
import com.optivus.bharathaat.data.local.entities.toProduct
import com.optivus.bharathaat.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val productDao: ProductDao
) {

    /**
     * Get products for current seller with offline support
     */
    fun getSellerProducts(): Flow<List<Product>> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            try {
                // Try Firestore first
                val firestoreProducts = firestore.collection("products")
                    .whereEqualTo("seller_id", currentUser.uid)
                    .orderBy("updated_at", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(Product::class.java) }

                // Cache locally
                productDao.insertProducts(firestoreProducts.map { it.toEntity() })
                emit(firestoreProducts)
            } catch (e: Exception) {
                // Fallback to local cache
                val cachedProducts = productDao.getProductsBySeller(currentUser.uid)
                emit(cachedProducts.map { it.toProduct() })
            }
        } else {
            emit(emptyList())
        }
    }

    /**
     * Get public products for browsing
     */
    fun getPublicProducts(limit: Int = 50): Flow<List<Product>> = flow {
        try {
            val products = firestore.collection("products")
                .whereEqualTo("is_active", true)
                .orderBy("updated_at", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Product::class.java) }

            // Cache active products
            productDao.insertProducts(products.map { it.toEntity() })
            emit(products)
        } catch (e: Exception) {
            // Fallback to cached active products
            val cachedProducts = productDao.getActiveProducts(limit)
            emit(cachedProducts.map { it.toProduct() })
        }
    }

    /**
     * Add new product
     */
    suspend fun addProduct(product: Product, imageUris: List<Uri>): Result<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val productId = UUID.randomUUID().toString()

                // Upload images
                val imageUrls = mutableListOf<String>()
                imageUris.forEachIndexed { index, uri ->
                    val imageUrl = uploadProductImage(currentUser.uid, productId, uri, index)
                    imageUrl.getOrNull()?.let { imageUrls.add(it) }
                }

                val newProduct = product.copy(
                    productId = productId,
                    sellerId = currentUser.uid,
                    imageUrls = imageUrls,
                    mainImageUrl = imageUrls.firstOrNull(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                // Save to Firestore
                firestore.collection("products")
                    .document(productId)
                    .set(newProduct)
                    .await()

                // Cache locally
                productDao.insertProduct(newProduct.toEntity())

                Result.success(productId)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update existing product
     */
    suspend fun updateProduct(product: Product): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null && currentUser.uid == product.sellerId) {
                val updatedProduct = product.copy(updatedAt = System.currentTimeMillis())

                // Update in Firestore
                firestore.collection("products")
                    .document(product.productId)
                    .set(updatedProduct)
                    .await()

                // Update local cache
                productDao.updateProduct(updatedProduct.toEntity())

                Result.success(Unit)
            } else {
                Result.failure(Exception("Unauthorized"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete product
     */
    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                // Check ownership
                val product = firestore.collection("products")
                    .document(productId)
                    .get()
                    .await()
                    .toObject(Product::class.java)

                if (product?.sellerId == currentUser.uid) {
                    // Delete from Firestore
                    firestore.collection("products")
                        .document(productId)
                        .delete()
                        .await()

                    // Delete from local cache
                    productDao.deleteProductById(productId)

                    // Delete images from Storage
                    deleteProductImages(currentUser.uid, productId)

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
     * Upload product image
     */
    private suspend fun uploadProductImage(
        sellerId: String,
        productId: String,
        imageUri: Uri,
        index: Int
    ): Result<String> {
        return try {
            val ref = storage.reference
                .child("products")
                .child(sellerId)
                .child(productId)
                .child("image_${index}_${System.currentTimeMillis()}.jpg")

            val uploadTask = ref.putFile(imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete product images
     */
    private suspend fun deleteProductImages(sellerId: String, productId: String) {
        try {
            val ref = storage.reference
                .child("products")
                .child(sellerId)
                .child(productId)

            val listResult = ref.listAll().await()
            listResult.items.forEach { item ->
                item.delete().await()
            }
        } catch (e: Exception) {
            // Handle silently as this is cleanup
        }
    }

    /**
     * Search products
     */
    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            // For simple search, we'll search by name and description
            // In production, consider using Algolia or similar for better search
            val products = firestore.collection("products")
                .whereEqualTo("is_active", true)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Product::class.java) }
                .filter {
                    it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
                }

            Result.success(products)
        } catch (e: Exception) {
            // Fallback to local search
            try {
                val cachedProducts = productDao.searchProducts(query)
                Result.success(cachedProducts.map { it.toProduct() })
            } catch (localE: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Get products by category
     */
    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val products = firestore.collection("products")
                .whereEqualTo("category", category)
                .whereEqualTo("is_active", true)
                .orderBy("updated_at", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Product::class.java) }

            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
