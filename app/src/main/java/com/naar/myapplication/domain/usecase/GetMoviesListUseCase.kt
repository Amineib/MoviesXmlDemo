package com.naar.myapplication.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.naar.myapplication.data.local.MovieDetailsEntity
import com.naar.myapplication.domain.models.Movie
import com.naar.myapplication.domain.models.MovieDetails
import com.naar.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetMoviesListUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun getMoviesList(query: String = ""): Flow<PagingData<MovieDetails>> = repository.getMoviesList(query)
}