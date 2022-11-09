package org.cream.corutines_practice_red.main.api

import org.cream.corutines_practice_red.main.model.Item

data class ImageSearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Item>
)