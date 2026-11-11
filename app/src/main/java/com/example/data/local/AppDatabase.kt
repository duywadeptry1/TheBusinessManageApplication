package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BusinessDao
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.PaymentShareEntity
import com.example.data.local.entity.RevenueEntity
import com.example.data.local.entity.ServiceEntity

@Database(
    entities = [
        OrderEntity::class,
        ServiceEntity::class,
        RevenueEntity::class,
        PaymentShareEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "business_manage_app_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
