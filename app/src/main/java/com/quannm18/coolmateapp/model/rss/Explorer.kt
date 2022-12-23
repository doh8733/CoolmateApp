package com.quannm18.coolmateapp.model.rss

data class Explorer(
    val description: String,
    val home_page_url: String,
    val items: List<Item>,
    val title: String,
    val version: String
)