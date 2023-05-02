package com.example.pixeltask.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product")
data class Product(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    @SerializedName("rating")
    @Embedded(prefix = "rating_")
    val rating: Rating
)

data class Rating(
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("count")
    val count: Int
)

