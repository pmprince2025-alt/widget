package com.widgetkit.domain.di

import com.widgetkit.domain.usecase.DeleteWidgetUseCase
import com.widgetkit.domain.usecase.GetAllWidgetsUseCase
import com.widgetkit.domain.usecase.SaveWidgetUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetAllWidgetsUseCase(useCase: GetAllWidgetsUseCase) = useCase

    @Provides
    @Singleton
    fun provideSaveWidgetUseCase(useCase: SaveWidgetUseCase) = useCase

    @Provides
    @Singleton
    fun provideDeleteWidgetUseCase(useCase: DeleteWidgetUseCase) = useCase
}
