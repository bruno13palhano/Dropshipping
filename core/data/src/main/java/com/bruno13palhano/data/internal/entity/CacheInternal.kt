package com.bruno13palhano.data.internal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Cache

@Entity
internal class CacheInternal(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val query: String
)

internal fun CacheInternal.asExternal() = Cache(
    id = id,
    query = query
)

internal fun Cache.asInternal() = CacheInternal(
    id = id,
    query = query
)