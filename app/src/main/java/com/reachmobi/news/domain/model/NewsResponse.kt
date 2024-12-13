package com.reachmobi.news.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

@Entity
data class Article(
    @PrimaryKey
    var id: Int = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    var isFavorite: Boolean = false
)

data class Source(
    val id: String,
    val name: String?
)
