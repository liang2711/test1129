package com.zywl.test1229.di

import android.app.Application
import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import com.zywl.test1229.data.PermissionState
import com.zywl.test1229.database.AppDatabase
import com.zywl.test1229.database.DataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {
    @Provides
    @PermissionStateFlow
    @Singleton
    fun providePermissionState(): MutableStateFlow<PermissionState> {
        return MutableStateFlow(PermissionState.None)
    }
    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database.db"
        ).build()
    }
    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): DataDao {
        return appDatabase.dataDao()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PermissionStateFlow