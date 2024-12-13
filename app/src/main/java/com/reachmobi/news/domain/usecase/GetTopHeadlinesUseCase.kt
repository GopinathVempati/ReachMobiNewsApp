package com.reachmobi.news.domain.usecase

import com.reachmobi.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke() = repository.fetchTopHeadlines()
}