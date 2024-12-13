package com.reachmobi.news.presentation.ui.screens

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.reachmobi.news.R
import com.reachmobi.news.presentation.ui.components.ArticleItem
import com.reachmobi.news.presentation.viewmodels.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(viewModel: NewsViewModel) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.favorite_articles)) })
    }) { padding ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_favorites_yet))
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(top = 75.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                items(favorites.size) { index ->
                    val article = favorites[index]
                    ArticleItem(viewModel, article, onFavoriteToggle = {
                        favorites[index].isFavorite = it
                        viewModel.toggleFavorite(article)
                    })
                }
            }
        }
    }
}