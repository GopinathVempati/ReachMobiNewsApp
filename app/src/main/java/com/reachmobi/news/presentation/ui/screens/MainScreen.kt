package com.reachmobi.news.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reachmobi.news.R
import com.reachmobi.news.data.repository.ApiResult
import com.reachmobi.news.domain.model.Article
import com.reachmobi.news.presentation.ui.components.ArticleItem
import com.reachmobi.news.presentation.ui.components.RetryContent
import com.reachmobi.news.presentation.viewmodels.NewsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: NewsViewModel) {
    val articles by viewModel.articles.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    var searchQuery by remember { mutableStateOf("") } // To store the search text
    var isSearchActive by remember { mutableStateOf(false) } // To toggle search mode
    var isRefreshClicked by remember { mutableStateOf(false) } // To toggle search mode

    Scaffold(topBar = {
        TopAppBar(title = {
            if (isSearchActive) {
                TextField(
                    value = searchQuery, onValueChange = {
                        searchQuery = it
                        viewModel.searchForNews(searchQuery)
                    },
                    placeholder = { Text(stringResource(R.string.search_news)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    singleLine = true, textStyle = TextStyle(fontSize = 16.sp)
                )
            } else {
                Text(stringResource(R.string.news_headlines))
            }
        }, actions = {
            if (isSearchActive) {
                IconButton(onClick = {
                    isSearchActive = false
                    searchQuery = ""
                }) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close_search))
                }
            } else {
                IconButton(onClick = {
                    isRefreshClicked = true
                    viewModel.loadHeadlines()
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.refresh))
                }
                IconButton(onClick = { isSearchActive = true }) {
                    Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                }
            }
            if (isRefreshClicked) {
                viewModel.loadHeadlines()
                isRefreshClicked = false
            }
        })
    }) { padding ->
        ArticleList(viewModel, articles, padding, onRetry = {
            isSearchActive = false
            searchQuery = ""
            viewModel.loadHeadlines()
        })
    }
}

@Composable
fun ArticleList(
    viewModel: NewsViewModel,
    uiState: ApiResult<List<Article>>,
    padding: PaddingValues,
    onRetry: () -> Unit
) {
    var networkStatus by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.isNetworkAvailable.onEach { isConnected ->
            networkStatus = isConnected
        }.launchIn(this)
    }

    if (!networkStatus)
        RetryContent(error = "No internet connection", onRetry = onRetry)
    else when (uiState) {
        is ApiResult.Success -> {
            val articles = uiState.data
            LazyColumn(
                modifier = Modifier.padding(
                    top = 75.dp, start = 16.dp, end = 16.dp, bottom = 16.dp
                )
            ) {
                items(articles.size) { index ->
                    val article = articles[index]
                    article.id = index
                    article.isFavorite = viewModel.isArticleFavorite(article)

                    ArticleItem(viewModel, article, onFavoriteToggle = {
                        articles[index].isFavorite = it
                        viewModel.toggleFavorite(article)
                    })
                }
            }
        }

        is ApiResult.Error -> {
            RetryContent(error = uiState.message, onRetry = onRetry)
        }

        is ApiResult.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
            }
        }
    }
}



