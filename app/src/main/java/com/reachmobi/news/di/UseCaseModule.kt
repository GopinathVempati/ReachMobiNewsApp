package com.reachmobi.news.di

import com.reachmobi.news.domain.repository.NewsRepository
import com.reachmobi.news.domain.usecase.GetTopHeadlinesUseCase
import com.reachmobi.news.domain.usecase.RemoveArticleUseCase
import com.reachmobi.news.domain.usecase.SaveArticleUseCase
import com.reachmobi.news.domain.usecase.SearchArticlesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetHeadlinesUseCase(repository: NewsRepository): GetTopHeadlinesUseCase {
        return GetTopHeadlinesUseCase(repository)
    }

    @Provides
    fun provideSearchArticlesUseCase(repository: NewsRepository): SearchArticlesUseCase {
        return SearchArticlesUseCase(repository)
    }

    @Provides
    fun provideSaveArticleUseCase(repository: NewsRepository): SaveArticleUseCase {
        return SaveArticleUseCase(repository)
    }

    @Provides
    fun provideRemoveArticleUseCase(repository: NewsRepository): RemoveArticleUseCase {
        return RemoveArticleUseCase(repository)
    }
}