package com.bruno13palhano.model

data class Product(
    val id: Long,
    val naturaCode: String,
    val name: String,
) {
    companion object {
        val EMPTY = Product(id = 0L, naturaCode = "", name = "")
    }
}