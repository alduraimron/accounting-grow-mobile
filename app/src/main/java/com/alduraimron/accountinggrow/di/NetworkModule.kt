package com.alduraimron.accountinggrow.di

import com.alduraimron.accountinggrow.BuildConfig
import com.alduraimron.accountinggrow.data.local.TokenManager
import com.alduraimron.accountinggrow.data.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.alduraimron.accountinggrow.data.remote.api.CategoryApi
import com.alduraimron.accountinggrow.data.remote.api.TransactionApi
import com.alduraimron.accountinggrow.data.remote.api.UserApi
import com.alduraimron.accountinggrow.data.remote.api.SavingApi
import com.alduraimron.accountinggrow.data.remote.api.BudgetApi
import com.alduraimron.accountinggrow.data.remote.api.ReminderApi
import com.alduraimron.accountinggrow.data.remote.api.ArticleApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenManager: TokenManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()

                // Add token to header if available and not auth endpoint
                if (!request.url.encodedPath.contains("/auth/")) {
                    tokenManager.getAccessToken()?.let { token ->
                        newRequest.addHeader("Authorization", "Bearer $token")
                    }
                }

                chain.proceed(newRequest.build())
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
    @Provides
    @Singleton
    fun provideTransactionApi(retrofit: Retrofit): TransactionApi {
        return retrofit.create(TransactionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSavingApi(retrofit: Retrofit): SavingApi {
        return retrofit.create(SavingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBudgetApi(retrofit: Retrofit): BudgetApi {
        return retrofit.create(BudgetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReminderApi(retrofit: Retrofit): ReminderApi {
        return retrofit.create(ReminderApi::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleApi(retrofit: Retrofit): ArticleApi {
        return retrofit.create(ArticleApi::class.java)
    }
}