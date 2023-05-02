package com.example.pixeltask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pixeltask.model.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
