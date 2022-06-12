package com.naar.myapplication.data.movies

import com.naar.myapplication.domain.models.MovieDetails
import com.naar.myapplication.util.Constants

data class MovieDetailResponse (
    val backdrop_path: String?,
    val budget: Int?,
    val homepage: String?,
    val id: Int?,
    val imdb_id: String?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val revenue: Int?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?,
        )


fun MovieDetailResponse.toDomain() : MovieDetails {
    return MovieDetails(
        backdrop_path, homepage, id, original_title, overview, popularity,
        poster_path = Constants.BASE_IMG_URL + poster_path
        , release_date, status, title, vote_average, vote_count
    )
}



