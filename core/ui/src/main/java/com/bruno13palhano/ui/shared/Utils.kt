package com.bruno13palhano.ui.shared

fun stringToFloat(value: String) =
    try { value.replace(",", ".").toFloat() }
    catch (ignored: Exception) { 0F }

fun stringToInt(value: String) = try { value.toInt() } catch (ignored: Exception) { 0 }

fun stringToLong(value: String) = try { value.toLong() } catch (ignored: Exception) { 0L }