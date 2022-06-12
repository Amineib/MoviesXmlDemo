package com.naar.myapplication.domain.usecase

import androidx.paging.PagingData
import com.naar.myapplication.domain.models.Movie
import com.naar.myapplication.domain.models.MovieDetails
import com.naar.myapplication.domain.repository.MovieRepository
import com.naar.myapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun getMovieDetails(id: Int): Flow<MovieDetails?> = repository.getMovieDetails(id)
    suspend fun updateMovie(movieDetails: MovieDetails) = repository.updateMovie(movieDetails)
    suspend fun addFavourite(id: Int) = repository.insertFavourite(id)
    suspend fun deleteFavourite(id: Int) = repository.removeFavourite(id)
    fun getFavourite(id: Int) = repository.getFavourite(id)


}