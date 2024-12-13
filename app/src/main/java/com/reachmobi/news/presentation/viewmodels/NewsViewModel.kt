package com.reachmobi.news.presentation.viewmodels

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.reachmobi.news.data.repository.ApiResult
import com.reachmobi.news.domain.model.Article
import com.reachmobi.news.domain.repository.NewsRepository
import com.reachmobi.news.domain.usecase.GetTopHeadlinesUseCase
import com.reachmobi.news.domain.usecase.RemoveArticleUseCase
import com.reachmobi.news.domain.usecase.SaveArticleUseCase
import com.reachmobi.news.domain.usecase.SearchArticlesUseCase
import com.reachmobi.news.utils.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    repository: NewsRepository,
    private val getHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val removeArticleUseCase: RemoveArticleUseCase,
    private val firebaseAnalytics: FirebaseAnalytics,
    networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    val isNetworkAvailable: StateFlow<Boolean> = networkConnectivityObserver.isConnected

    private val _articles = MutableStateFlow<ApiResult<List<Article>>>(ApiResult.Loading)
    val articles: StateFlow<ApiResult<List<Article>>> = _articles

    private val _favorites = repository.getSavedArticles().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val favorites: StateFlow<List<Article>> get() = _favorites

    init {
        loadHeadlines()
    }

    fun loadHeadlines() {
        viewModelScope.launch {
            logEvent("fetch_headlines")
            _articles.value = ApiResult.Loading
            val result = getHeadlinesUseCase.invoke()
            _articles.value = result
        }
    }

    fun searchForNews(query: String) {
        viewModelScope.launch {
            logEvent("search_articles", mapOf("query" to query))
            _articles.value = ApiResult.Loading
            val result = searchArticlesUseCase(query)
            _articles.value = result
        }
    }

    fun isArticleFavorite(article: Article): Boolean {
        val favorites = favorites.value
        val result = favorites.any { it.url == article.url }
        return result
    }

    fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            if (isArticleFavorite(article)) {
                removeArticleUseCase.invoke(article)
                article.title?.let {
                    logEvent("favorite_removed", mapOf("article_title" to it))
                }
            } else {
                saveArticleUseCase.invoke(article)
                article.title?.let {
                    logEvent("favorite_added", mapOf("article_title" to article.title))
                }
            }
        }
    }

    fun logEvent(event: String, params: Map<String, String> = emptyMap()) {
        val bundle = Bundle().apply {
            params.forEach { putString(it.key, it.value) }
        }
        firebaseAnalytics.logEvent(event, bundle)
    }
}
