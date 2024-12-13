package com.reachmobi.news.domain.usecase

import com.reachmobi.news.domain.model.Article
import com.reachmobi.news.domain.repository.NewsRepository
import javax.inject.Inject

class SaveArticleUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(article: Article) = repository.saveArticle(article)
    operator fun invoke() = repository.getSavedArticles()
}