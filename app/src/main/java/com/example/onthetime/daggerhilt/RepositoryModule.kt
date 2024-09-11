package com.example.onthetime.daggerhilt

import com.example.onthetime.repository.AuthRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//
//@InstallIn(SingletonComponent::class)
//@Module
class RepositoryModule {
//    @Provides
//    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepository()
    }
}