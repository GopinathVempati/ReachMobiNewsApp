package com.reachmobi.news.data.repository


import com.reachmobi.news.data.local.NewsDao
import com.reachmobi.news.data.network.NewsApi
import com.reachmobi.news.domain.model.Article
import com.reachmobi.news.domain.repository.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val dao: NewsDao,
) : NewsRepository {

    override suspend fun fetchTopHeadlines(): ApiResult<List<Article>> {
        return try {
            val response = retryIO { api.getTopHeadlines() }
            if (response.status == "ok" && response.articles.isNotEmpty()) {
                ApiResult.Success(response.articles)
            } else {
                ApiResult.Error("Error fetching articles")
            }
        } catch (e: IOException) {
            ApiResult.Error(IOException("Network error occurred. Please try again.", e).toString())
        } catch (e: Exception) {
            ApiResult.Error(e.toString())
        }
    }

    override suspend fun searchForNews(query: String): ApiResult<List<Article>> {
        return try {
            val response = retryIO { api.searchForNews(query) }
            if (response.status == "ok" && response.articles.isNotEmpty()) {
                ApiResult.Success(response.articles)
            } else {
                ApiResult.Error("No articles found")
            }
        } catch (e: IOException) {
            ApiResult.Error(IOException("Network error occurred. Please try again.", e).toString())
        } catch (e: Exception) {
            ApiResult.Error(e.toString())
        }
    }

    override suspend fun saveArticle(article: Article) {
        dao.saveArticle(article)
    }

    override suspend fun removeArticle(article: Article) {
        dao.deleteArticle(article)
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return dao.getAllSavedArticles()
    }

    private suspend fun <T> retryIO(
        times: Int = 3,
        initialDelay: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: IOException) {
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong()
            }
        }
        return block()
    }
}