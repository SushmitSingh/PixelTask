package com.example.pixeltask.network

import com.example.pixeltask.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("products")
    suspend fun getProducts(): Response<List<Product>>
}
