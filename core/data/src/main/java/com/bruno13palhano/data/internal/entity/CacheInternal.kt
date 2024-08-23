package com.bruno13palhano.data.internal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Cache

@Entity
internal class CacheInternal(
    @PrimaryKey
    val query: String
)

internal fun CacheInternal.asExternal() = Cache(query = query)

internal fun Cache.asInternal() = CacheInternal(query = query)