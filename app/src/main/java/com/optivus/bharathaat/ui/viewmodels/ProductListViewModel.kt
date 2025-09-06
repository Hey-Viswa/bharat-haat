package com.optivus.bharathaat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optivus.bharathaat.data.models.Product
import com.optivus.bharathaat.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init {
        // Observe search query changes and reload products with debounce
        _searchQuery
            .debounce(300) // Wait 300ms after user stops typing
            .onEach { loadProducts() }
            .launchIn(viewModelScope)

        // Observe category changes
        _selectedCategory
            .onEach { loadProducts() }
            .launchIn(viewModelScope)

        // Initial load
        loadProducts()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun retry() {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                _uiState.value = ProductListUiState.Loading

                val query = _searchQuery.value.trim()
                val category = _selectedCategory.value

                val products = when {
                    query.isNotBlank() -> {
                        // Search products
                        val result = productRepository.searchProducts(query)
                        if (result.isSuccess) {
                            val searchResults = result.getOrNull() ?: emptyList()
                            // Further filter by category if selected
                            if (category != null) {
                                searchResults.filter { it.category.equals(category, ignoreCase = true) }
                            } else {
                                searchResults
                            }
                        } else {
                            throw result.exceptionOrNull() ?: Exception("Search failed")
                        }
                    }
                    category != null -> {
                        // Get products by category
                        val result = productRepository.getProductsByCategory(category)
                        if (result.isSuccess) {
                            result.getOrNull() ?: emptyList()
                        } else {
                            throw result.exceptionOrNull() ?: Exception("Failed to load category products")
                        }
                    }
                    else -> {
                        // Get all public products using Flow
                        val productsFlow = productRepository.getPublicProducts()
                        var productsList: List<Product> = emptyList()
                        
                        productsFlow.collect { products ->
                            productsList = products
                        }
                        productsList
                    }
                }

                _uiState.value = ProductListUiState.Success(products)

            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(
                    e.message ?: "Failed to load products"
                )
            }
        }
    }
}

sealed class ProductListUiState {
    object Loading : ProductListUiState()
    data class Success(val products: List<Product>) : ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
}
