package com.reachmobi.news.domain.repository

import com.reachmobi.news.data.repository.ApiResult
import com.reachmobi.news.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun fetchTopHeadlines(): ApiResult<List<Article>>
    suspend fun searchForNews(query: String): ApiResult<List<Article>>
    suspend fun saveArticle(article: Article)
    suspend fun removeArticle(article: Article)
    fun getSavedArticles(): Flow<List<Article>>
}
