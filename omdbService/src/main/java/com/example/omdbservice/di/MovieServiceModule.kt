package com.example.omdbservice.di

import com.example.omdbservice.OmdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MovieServiceModule {
    @Provides
    @Singleton
    fun provideOmdbApi(retrofit: Retrofit): OmdbApi {
        return retrofit.create(OmdbApi::class.java)
    }
}