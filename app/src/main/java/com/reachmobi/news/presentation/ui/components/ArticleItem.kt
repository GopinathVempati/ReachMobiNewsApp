package com.reachmobi.news.presentation.ui.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.reachmobi.news.R
import com.reachmobi.news.domain.model.Article
import com.reachmobi.news.presentation.viewmodels.NewsViewModel
import com.reachmobi.news.utils.Utils
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItem(viewModel: NewsViewModel, article: Article, onFavoriteToggle: (Boolean) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var shouldBottomSheetShow by remember { mutableStateOf(false) }
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    isFavorite = article.isFavorite

    animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    if (shouldBottomSheetShow) {
        ModalBottomSheet(onDismissRequest = { shouldBottomSheetShow = false },
            sheetState = sheetState,
            content = {
                BottomSheetContent(article = article, onReadFullStoryButtonClicked = {
                    article.url?.let { Utils.openArticleInBrowser(context, it) }
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) shouldBottomSheetShow = false
                    }
                })
            })
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = {
                shouldBottomSheetShow = true
                article.title?.let { title ->
                    mapOf("article_title" to title).let {
                        viewModel.logEvent("article_clicked", it)
                    }
                }
            }),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row {
            Text(
                text = article.title.toString(),
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            )

            val animatedScale = remember { Animatable(1f) }

            LaunchedEffect(article.isFavorite) {
                try {
                    animatedScale.animateTo(
                        targetValue = if (article.isFavorite) 1.4f else 1f,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                    animatedScale.animateTo(1f)
                } catch (e: CancellationException) {
                    Log.e("AnimationError", "Animation interrupted: ${e.message}", e)
                }
            }

            IconButton(modifier = Modifier
                .size(50.dp)
                .scale(animatedScale.value)
                .align(Alignment.CenterVertically), onClick = {
                isFavorite = !isFavorite
                onFavoriteToggle(isFavorite)
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorite),
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}