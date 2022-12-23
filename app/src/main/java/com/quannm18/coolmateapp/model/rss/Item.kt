package com.quannm18.coolmateapp.model.rss

data class Item(
    val author: Author,
    val date_published: String,
    val guid: String,
    val summary: String,
    val title: String,
    val url: String)