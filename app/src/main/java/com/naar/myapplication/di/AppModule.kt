package com.naar.myapplication.di

import com.naar.myapplication.api.TmdbService
import com.naar.myapplication.data.local.FavouriteMoviesDatabase
import com.naar.myapplication.data.movies.MoviesRepositoryImp
import com.naar.myapplication.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideMovieRepository(api : TmdbService, database: FavouriteMoviesDatabase) : MovieRepository = MoviesRepositoryImp(api,database)
}