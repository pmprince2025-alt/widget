package com.widgetkit.data.di

import com.widgetkit.data.repository.WidgetRepositoryImpl
import com.widgetkit.domain.repository.WidgetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindWidgetRepository(impl: WidgetRepositoryImpl): WidgetRepository
}
