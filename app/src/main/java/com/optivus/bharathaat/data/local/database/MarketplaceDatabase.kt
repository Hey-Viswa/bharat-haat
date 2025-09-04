package com.optivus.bharathaat.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.optivus.bharathaat.data.local.dao.UserDao
import com.optivus.bharathaat.data.local.dao.ProductDao
import com.optivus.bharathaat.data.local.dao.OrderDao
import com.optivus.bharathaat.data.local.entities.UserEntity
import com.optivus.bharathaat.data.local.entities.ProductEntity
import com.optivus.bharathaat.data.local.entities.OrderEntity
import com.optivus.bharathaat.data.local.entities.UserEntityConverters
import com.optivus.bharathaat.data.local.entities.ProductEntityConverters
import com.optivus.bharathaat.data.local.entities.OrderEntityConverters

@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        OrderEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    UserEntityConverters::class,
    ProductEntityConverters::class,
    OrderEntityConverters::class
)
abstract class MarketplaceDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: MarketplaceDatabase? = null

        fun getDatabase(context: Context): MarketplaceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarketplaceDatabase::class.java,
                    "marketplace_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
