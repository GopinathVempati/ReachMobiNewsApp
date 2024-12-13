package com.reachmobi.news.domain.usecase

import com.reachmobi.news.domain.repository.NewsRepository
import javax.inject.Inject

class SearchArticlesUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(query: String) = repository.searchForNews(query)
}