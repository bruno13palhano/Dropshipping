package com.bruno13palhano.data.internal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.internal.CacheDataSource
import com.bruno13palhano.data.internal.entity.CacheInternal
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CacheDao : CacheDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(data: CacheInternal)

    @Update
    override suspend fun update(data: CacheInternal)

    @Query("DELETE FROM CacheInternal WHERE id = :id")
    override suspend fun delete(id: Long)

    @Query("SELECT * FROM CacheInternal WHERE id = :id")
    override fun get(id: Long): Flow<CacheInternal>

    @Query("SELECT * FROM CacheInternal ORDER BY id DESC")
    override fun getAll(): Flow<List<CacheInternal>>
}