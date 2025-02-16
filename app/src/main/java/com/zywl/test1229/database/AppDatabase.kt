package com.zywl.test1229.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zywl.test1229.bean.StakeInfo

@Database(entities = [StakeInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao():DataDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}