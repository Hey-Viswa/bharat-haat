package com.optivus.bharathaat.data.local.dao

import androidx.room.*
import com.optivus.bharathaat.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getProductById(productId: String): ProductEntity?

    @Query("SELECT * FROM products WHERE productId = :productId")
    fun getProductByIdFlow(productId: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE sellerId = :sellerId ORDER BY updatedAt DESC")
    suspend fun getProductsBySeller(sellerId: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE sellerId = :sellerId ORDER BY updatedAt DESC")
    fun getProductsBySellerFlow(sellerId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY updatedAt DESC LIMIT :limit")
    suspend fun getActiveProducts(limit: Int = 50): List<ProductEntity>

    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY updatedAt DESC LIMIT :limit")
    fun getActiveProductsFlow(limit: Int = 50): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category = :category AND isActive = 1 ORDER BY updatedAt DESC")
    suspend fun getProductsByCategory(category: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE category = :category AND isActive = 1 ORDER BY updatedAt DESC")
    fun getProductsByCategoryFlow(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' AND isActive = 1")
    suspend fun searchProducts(query: String): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE productId = :productId")
    suspend fun deleteProductById(productId: String)

    @Query("DELETE FROM products WHERE sellerId = :sellerId")
    suspend fun deleteProductsBySeller(sellerId: String)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}
