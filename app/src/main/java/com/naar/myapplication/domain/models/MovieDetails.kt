package com.naar.myapplication.domain.models

import com.naar.myapplication.data.local.MovieDetailsEntity

data class MovieDetails(
    val backdrop_path: String?,
    val homepage: String?,
    val id: Int?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val status: String?,
    val title: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val isFavourite: Boolean? = false
)


fun MovieDetails.toDatabaseEntity() :MovieDetailsEntity{
    return MovieDetailsEntity(
        id!!, backdrop_path, budget = 0, homepage, imdb_id = "", original_title,original_title,overview,popularity,poster_path,release_date, revenue = 0,0,status,tagline = null,
        title, video = null,vote_average,vote_count,isFavourite
    )
}