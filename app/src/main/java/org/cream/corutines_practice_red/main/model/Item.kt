package org.cream.corutines_practice_red.main.model

import com.google.gson.annotations.SerializedName

data class Item(
    val title: String,
    val link: String,
    val thumbnail: String,
    val sizeHeight: Int,
    val sizeWidth: Int
)
