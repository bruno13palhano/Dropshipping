package com.bruno13palhano.data.internal.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bruno13palhano.data.internal.entity.ProductInternal

@ProvidedTypeConverter
internal class Converters {
    @TypeConverter
    fun productInternalToString(product: ProductInternal): String {
        return "${product.id},${product.naturaCode},${product.name}"
    }

    @TypeConverter
    fun stringToProductInternal(string: String): ProductInternal {
        val list = string.split(",")
        return ProductInternal(
            id = list[0].toLong(),
            naturaCode = list[1],
            name = list[2]
        )
    }
}