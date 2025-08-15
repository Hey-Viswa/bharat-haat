package com.optivus.bharat_haat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                _homeState.value = HomeState.Loading
                // Simulate loading products - replace with actual API call
                val sampleProducts = getSampleProducts()
                _products.value = sampleProducts
                _homeState.value = HomeState.Success
            } catch (e: Exception) {
                _homeState.value = HomeState.Error(e.message ?: "Failed to load products")
            }
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            try {
                _homeState.value = HomeState.Loading
                val allProducts = getSampleProducts()
                val filteredProducts = if (query.isBlank()) {
                    allProducts
                } else {
                    allProducts.filter { product ->
                        product.name.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
                    }
                }
                _products.value = filteredProducts
                _homeState.value = HomeState.Success
            } catch (e: Exception) {
                _homeState.value = HomeState.Error("Search failed: ${e.message}")
            }
        }
    }

    fun refreshProducts() {
        loadProducts()
    }

    private fun getSampleProducts(): List<Product> {
        return listOf(
            Product(
                id = "1",
                name = "Handwoven Silk Saree",
                description = "Beautiful traditional silk saree with intricate patterns",
                price = 2999.0,
                imageUrl = "https://example.com/saree.jpg",
                category = "Clothing",
                rating = 4.5f,
                inStock = true,
                seller = "Artisan Crafts"
            ),
            Product(
                id = "2",
                name = "Brass Kitchen Utensils Set",
                description = "Traditional brass utensils for authentic cooking",
                price = 1299.0,
                imageUrl = "https://example.com/brass.jpg",
                category = "Kitchen",
                rating = 4.2f,
                inStock = true,
                seller = "Heritage Metals"
            ),
            Product(
                id = "3",
                name = "Wooden Handicraft Sculpture",
                description = "Hand-carved wooden sculpture by local artisans",
                price = 899.0,
                imageUrl = "https://example.com/sculpture.jpg",
                category = "Handicrafts",
                rating = 4.8f,
                inStock = false,
                seller = "Wood Works India"
            ),
            Product(
                id = "4",
                name = "Organic Spice Collection",
                description = "Premium quality organic spices from Indian farms",
                price = 499.0,
                imageUrl = "https://example.com/spices.jpg",
                category = "Food",
                rating = 4.6f,
                inStock = true,
                seller = "Farm Fresh"
            ),
            Product(
                id = "5",
                name = "Block Print Cotton Kurta",
                description = "Traditional block print cotton kurta for men",
                price = 799.0,
                imageUrl = "https://example.com/kurta.jpg",
                category = "Clothing",
                rating = 4.3f,
                inStock = true,
                seller = "Traditional Wear"
            ),
            Product(
                id = "6",
                name = "Handmade Pottery Set",
                description = "Beautiful handmade pottery for home decoration",
                price = 1199.0,
                imageUrl = "https://example.com/pottery.jpg",
                category = "Home Decor",
                rating = 4.7f,
                inStock = true,
                seller = "Clay Creations"
            )
        )
    }
}

// Home screen states
sealed class HomeState {
    object Loading : HomeState()
    object Success : HomeState()
    data class Error(val message: String) : HomeState()
}

// Product data class
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val rating: Float,
    val inStock: Boolean,
    val seller: String
)
