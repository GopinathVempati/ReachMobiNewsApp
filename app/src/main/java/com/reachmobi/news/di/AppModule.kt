package com.reachmobi.news.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import com.reachmobi.news.BuildConfig
import com.reachmobi.news.data.local.NewsDao
import com.reachmobi.news.data.local.NewsDatabase
import com.reachmobi.news.data.network.NewsApi
import com.reachmobi.news.data.repository.NewsRepositoryImpl
import com.reachmobi.news.domain.repository.NewsRepository
import com.reachmobi.news.utils.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): NewsApi {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val cache = Cache(File("cache_directory"), cacheSize)

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(context, NewsDatabase::class.java, "news_db").build()

    @Provides
    fun provideNewsDao(db: NewsDatabase): NewsDao = db.newsDao()

    @Provides
    @Singleton
    fun provideNewsRepository(
        apiService: NewsApi,
        db: NewsDatabase,
    ): NewsRepository {
        return NewsRepositoryImpl(apiService, db.newsDao())
    }

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }
}
