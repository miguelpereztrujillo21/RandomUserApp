package com.mpt.randomuserapp.di

import android.content.Context
import com.mpt.randomuserapp.BuildConfig
import com.mpt.randomuserapp.RandomUserApp
import com.mpt.randomuserapp.api.Api
import com.mpt.randomuserapp.api.ApiRepository
import com.mpt.randomuserapp.api.ApiRepositoryImpl
import com.mpt.randomuserapp.api.UserPagingSource
import com.mpt.randomuserapp.api.UserPagingSourceFactory
import com.mpt.randomuserapp.data.UsersPreferences
import com.mpt.randomuserapp.domain.UserPreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun provideUsersPreferences(@ApplicationContext context: Context): UsersPreferences{
        return UserPreferencesImpl(context)
    }
}
