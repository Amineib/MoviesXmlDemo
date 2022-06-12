package com.naar.myapplication.domain.repository

import androidx.paging.PagingData
import com.naar.myapplication.data.local.FavouritesDao
import com.naar.myapplication.data.local.FavouritesEntity
import com.naar.myapplication.data.local.MovieDetailsEntity
import com.naar.myapplication.domain.models.Movie
import com.naar.myapplication.domain.models.MovieDetails
import com.naar.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesList(query: String) : Flow<PagingData<MovieDetails>>
    fun searchMovie(query: String): Flow<PagingData<MovieDetails>>
    fun getMovieDetails(id: Int) : Flow<MovieDetails?>
    suspend fun updateMovie(movie: MovieDetails)

    suspend fun insertFavourite(id: Int)
    suspend fun removeFavourite(id: Int)
    fun getFavourite(id: Int): Flow<FavouritesEntity?>


}