package com.naar.myapplication.data.movies

import com.google.gson.annotations.SerializedName
import com.naar.myapplication.domain.models.MovieList

data class MovieListResponse(
    val page: Int,
    val results: List<MovieResponse>,
    @field:SerializedName("total_pages") val totalPages: Int,
    @field:SerializedName("total_results") val totalResults: Int
)

fun MovieListResponse.toDomain() : MovieList{
    return MovieList(
        page,
        results.mapNotNull {
            it.toDomain()
        }
    )
}


