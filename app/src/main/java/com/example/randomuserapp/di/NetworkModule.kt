package com.example.randomuserapp.di

import com.example.randomuserapp.BuildConfig
import com.example.randomuserapp.RandomUserApp
import com.example.randomuserapp.api.Api
import com.example.randomuserapp.api.ApiRepository
import com.example.randomuserapp.api.ApiRepositoryImpl
import com.example.randomuserapp.api.UserPagingSource
import com.example.randomuserapp.api.UserPagingSourceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
    @Provides
    fun provideUserPagingSourceFactory(apiRepository: ApiRepository): UserPagingSourceFactory {
        return UserPagingSourceFactory(apiRepository)
    }

    @Provides
    @Singleton
    fun provideApiRepository(api: Api): ApiRepository {
        return ApiRepositoryImpl(api)
    }
}
