package com.naar.myapplication.di

import android.content.Context
import com.naar.myapplication.data.local.MovieDetailsDao
import com.naar.myapplication.data.local.FavouriteMoviesDatabase
import com.naar.myapplication.data.local.FavouritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): FavouriteMoviesDatabase {
        return FavouriteMoviesDatabase.getInstance(context)
    }

    @Provides
    fun provideFavMovieDao(favouriteMoviesDatabase: FavouriteMoviesDatabase): MovieDetailsDao {
        return favouriteMoviesDatabase.favouriteMoviesDao()
    }

    @Provides
    fun provideFavouritesListDao(favouriteMoviesDatabase: FavouriteMoviesDatabase): FavouritesDao {
        return favouriteMoviesDatabase.favouritesListDao()
    }


}