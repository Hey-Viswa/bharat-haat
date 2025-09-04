package com.optivus.bharathaat.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.optivus.bharathaat.data.local.dao.OrderDao
import com.optivus.bharathaat.data.local.dao.ProductDao
import com.optivus.bharathaat.data.local.dao.UserDao
import com.optivus.bharathaat.data.local.database.MarketplaceDatabase
import com.optivus.bharathaat.data.repository.OrderRepository
import com.optivus.bharathaat.data.repository.ProductRepository
import com.optivus.bharathaat.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMarketplaceDatabase(@ApplicationContext context: Context): MarketplaceDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MarketplaceDatabase::class.java,
            "marketplace_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: MarketplaceDatabase): UserDao = database.userDao()

    @Provides
    fun provideProductDao(database: MarketplaceDatabase): ProductDao = database.productDao()

    @Provides
    fun provideOrderDao(database: MarketplaceDatabase): OrderDao = database.orderDao()
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        userDao: UserDao
    ): UserRepository = UserRepository(firebaseAuth, firestore, storage, userDao)

    @Provides
    @Singleton
    fun provideProductRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        productDao: ProductDao
    ): ProductRepository = ProductRepository(firebaseAuth, firestore, storage, productDao)

    @Provides
    @Singleton
    fun provideOrderRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        orderDao: OrderDao
    ): OrderRepository = OrderRepository(firebaseAuth, firestore, orderDao)
}
