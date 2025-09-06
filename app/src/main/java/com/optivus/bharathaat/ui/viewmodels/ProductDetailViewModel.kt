package com.optivus.bharathaat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.optivus.bharathaat.data.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = ProductDetailUiState.Loading

                // Get product from Firestore
                val document = firestore.collection("products")
                    .document(productId)
                    .get()
                    .await()

                if (document.exists()) {
                    val product = document.toObject(Product::class.java)
                    if (product != null && product.isActive) {
                        // Increment view count
                        incrementViewCount(productId)
                        _uiState.value = ProductDetailUiState.Success(product)
                    } else {
                        _uiState.value = ProductDetailUiState.Error("Product is no longer available")
                    }
                } else {
                    _uiState.value = ProductDetailUiState.Error("Product not found")
                }

            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState.Error(
                    e.message ?: "Failed to load product details"
                )
            }
        }
    }

    private fun incrementViewCount(productId: String) {
        viewModelScope.launch {
            try {
                firestore.collection("products")
                    .document(productId)
                    .update(
                        mapOf(
                            "view_count" to com.google.firebase.firestore.FieldValue.increment(1)
                        )
                    )
                    .await()
            } catch (e: Exception) {
                // Silently handle - view count is not critical
            }
        }
    }

    fun addToCart(product: Product, quantity: Int) {
        // TODO: Implement add to cart functionality
        // This will be implemented in Phase 3 (Shopping Cart)
    }

    fun addToFavorites(product: Product) {
        // TODO: Implement add to favorites functionality  
        // This will be implemented in a later phase
    }

    fun shareProduct(product: Product) {
        // TODO: Implement product sharing functionality
        // This will be implemented in a later phase
    }
}

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}
