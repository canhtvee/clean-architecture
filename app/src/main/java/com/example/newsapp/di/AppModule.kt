package com.example.newsapp.di

import com.example.newsapp.data.repositories.ExploreTopicRepository
import com.example.newsapp.data.repositories.HomeRepository
import com.example.newsapp.utils.SourcePlanning
import com.example.newsapp.viewmodels.ExploreTopicViewModel
import com.example.newsapp.viewmodels.ExploreViewModel
import com.example.newsapp.viewmodels.HeadlineViewModel
import com.example.newsapp.viewmodels.WebViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSourcePlanning(): SourcePlanning = SourcePlanning()

    @Singleton
    @Provides
    fun provideHeadlineViewModel(
        homeRepository: HomeRepository,
        sourcePlanning: SourcePlanning
    ): HeadlineViewModel = HeadlineViewModel(homeRepository, sourcePlanning)

    @Singleton
    @Provides
    fun provideExploreViewModel(
        homeRepository: HomeRepository,
        sourcePlanning: SourcePlanning
    ): ExploreViewModel = ExploreViewModel(homeRepository, sourcePlanning)

    @Singleton
    @Provides
    fun provideExploreTopicViewModel(exploreTopicRepository: ExploreTopicRepository
    ): ExploreTopicViewModel = ExploreTopicViewModel(exploreTopicRepository)

    @Singleton
    @Provides
    fun provideWebViewModel(): WebViewModel = WebViewModel()
}