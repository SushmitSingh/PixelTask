package com.example.pixeltask.repository

import android.util.Log
import com.example.pixeltask.database.ProductDao
import com.example.pixeltask.model.Product
import com.example.pixeltask.network.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductRepository(private val productDao: ProductDao) {

    private val productService: ProductService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ProductService::class.java)
    }

    fun getProductsFromRoom(): List<Product> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                productDao.getAll()
            }
        }
    }

    suspend fun downloadProducts(): List<Product> {
        return try {
            withContext(Dispatchers.IO) {
                val response = productService.getProducts()
                if (response.isSuccessful) {
                    val products = response.body()
                    if (products != null) {
                        productDao.deleteAll()
                        productDao.insertAll(products)
                        return@withContext products
                    }
                }
                emptyList()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                // Handle exception
                Log.e("ProductRepository", "Error fetching products", e)
            }
            emptyList()
        }
    }

    fun isDataDownloaded(): Boolean {
        return runBlocking {
            withContext(Dispatchers.IO) {
                productDao.getAll().isNotEmpty()
            }
        }
    }

}
