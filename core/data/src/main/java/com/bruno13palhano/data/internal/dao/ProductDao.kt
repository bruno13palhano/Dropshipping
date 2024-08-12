package com.bruno13palhano.data.internal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.internal.ProductDataSource
import com.bruno13palhano.data.internal.entity.ProductInternal
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductDao : ProductDataSource {
    @Insert
    override suspend fun insert(data: ProductInternal)

    @Update
    override suspend fun update(data: ProductInternal)

    @Query("DELETE FROM ProductInternal WHERE id = :id")
    override suspend fun delete(id: Long)

    @Query("SELECT * FROM ProductInternal WHERE id = :id")
    override fun get(id: Long): Flow<ProductInternal>

    @Query("SELECT * FROM ProductInternal ORDER BY id DESC")
    override fun getAll(): Flow<List<ProductInternal>>

    @Query("SELECT * FROM ProductInternal WHERE naturaCode LIKE '%' || :query || '%'" +
            " OR name LIKE '%' || :query || '%' ORDER BY name DESC, naturaCode DESC")
    override fun search(query: String): Flow<List<ProductInternal>>
}