package com.example.pixeltask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixeltask.model.Product
import com.example.pixeltask.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    var networkAvailable = false
    fun setNetworkState(state: Boolean) {
        networkAvailable = state
    }

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    // Call getProducts
    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            // Check if data is downloaded and available in Room database
            val downloadedProducts = if (productRepository.isDataDownloaded()) {
                productRepository.getProductsFromRoom()
            } else {
                productRepository.downloadProducts()
            }
            // If downloaded data is not null, set the LiveData value
            _products.value = downloadedProducts
        }
    }
}
