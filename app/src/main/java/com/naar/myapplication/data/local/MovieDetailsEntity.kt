package com.naar.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.naar.myapplication.domain.models.MovieDetails

@Entity
data class MovieDetailsEntity(
    @PrimaryKey val id: Int,
    val backdrop_path: String?,
    val budget: Int?,
    val homepage: String?,
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
    val isFavourite: Boolean? = false
)


fun MovieDetailsEntity.toDomain() : MovieDetails{
    return MovieDetails(
        backdrop_path,homepage, id, original_title, overview, popularity, poster_path, release_date, status, title, vote_average, vote_count, isFavourite
    )
}