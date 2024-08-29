package com.bruno13palhano.data.internal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.internal.datasource.CacheDataSource
import com.bruno13palhano.data.internal.entity.CacheInternal
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CacheDao : CacheDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(data: CacheInternal)

    @Update
    override suspend fun update(data: CacheInternal)

    @Query("DELETE FROM CacheInternal WHERE `query` = :query")
    override suspend fun delete(query: String)

    @Query("SELECT * FROM CacheInternal")
    override fun getAll(): Flow<List<CacheInternal>>
}