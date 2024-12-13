package com.reachmobi.news.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reachmobi.news.domain.model.Article

@Composable
fun BottomSheetContent(
    article: Article, onReadFullStoryButtonClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = article.title.toString(), style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = article.source?.name.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.paddingFromBaseline(top = 16.dp)
                    )
                    Text(
                        text = article.author.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.paddingFromBaseline(top = 16.dp)
                    )
                }
                Text(
                    text = article.publishedAt.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .paddingFromBaseline(bottom = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onReadFullStoryButtonClicked
            ) {
                Text(text = "Read Full Story")
            }
        }
    }
}