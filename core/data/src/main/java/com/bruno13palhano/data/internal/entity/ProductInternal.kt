package com.bruno13palhano.data.internal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.model.Product

@Entity
data class ProductInternal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val naturaCode: String,
    val name: String
)

internal fun ProductInternal.asExternal() = Product(
    id = id,
    naturaCode = naturaCode,
    name = name
)

internal fun Product.asInternal() = ProductInternal(
    id = id,
    naturaCode = naturaCode,
    name = name
)
