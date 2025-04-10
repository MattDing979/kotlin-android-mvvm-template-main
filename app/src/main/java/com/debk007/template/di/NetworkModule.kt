package com.debk007.template.di

import com.debk007.template.BuildConfig
import com.debk007.template.BuildConfig.BASE_URL
import com.debk007.template.datasource.DataSource
import com.debk007.template.datasource.DataSourceImpl
import com.debk007.template.network.ApiService
import com.debk007.template.repository.Repository
import com.debk007.template.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val DEFAULT_TIMEOUT = 20_000L

    @Provides
    @Singleton
    fun providesRetrofit(): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .apply {
                connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesRepository(repositoryImpl: RepositoryImpl): Repository = repositoryImpl

    @Provides
    @Singleton
    fun providesDataSource(dataSourceImpl: DataSourceImpl): DataSource = dataSourceImpl
}