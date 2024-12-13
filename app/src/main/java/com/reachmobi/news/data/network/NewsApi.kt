package com.reachmobi.news.data.network

import com.reachmobi.news.BuildConfig
import com.reachmobi.news.domain.model.NewsResponse
import com.reachmobi.news.utils.NetworkService.COUNTRY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = COUNTRY,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): NewsResponse

    @GET("everything")
    suspend fun searchForNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ) : NewsResponse
}