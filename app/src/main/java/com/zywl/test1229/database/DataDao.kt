package com.zywl.test1229.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zywl.test1229.bean.StakeInfo

@Dao
interface DataDao {
    @Insert
    suspend fun insert(data:StakeInfo)
    @Insert
    suspend fun insertAll(data:List<StakeInfo>)
    @Query("SELECT * FROM info_table")
    suspend fun getAllStakeInfo(): List<StakeInfo>
    @Query("DELETE FROM info_table")
    suspend fun deleteAll()
}