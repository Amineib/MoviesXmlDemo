package com.naar.myapplication.data.movies

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.naar.myapplication.data.local.MovieDetailsEntity
import com.naar.myapplication.domain.models.Movie
import com.naar.myapplication.util.Constants

data class MovieResponse (
    val adult: Boolean?,
    @field:SerializedName("backdrop_path") val backdropPath: String?,
    @field:SerializedName("genre_ids") val genreIds: List<Int>?,
    val id: Int?,
    @field:SerializedName("original_language") val originalLanguage: String?,
    @field:SerializedName("original_title") val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("release_date") val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    @field:SerializedName("vote_average") val voteAverage: Double?,
    @field:SerializedName("vote_count") val voteCount: Int?
)

fun MovieResponse.toDomain(): Movie{

    return Movie(
        backdropPath, id, originalTitle, overview, popularity,
        posterPath = Constants.BASE_IMG_URL + posterPath,
        releaseDate, title, voteAverage
    )
}

fun MovieResponse.toDatabaseEntity(): MovieDetailsEntity{
    Log.d("PosterPath",Constants.BASE_IMG_URL + backdropPath + "----" +Constants.BASE_IMG_URL + posterPath )
    return MovieDetailsEntity(
       id?:0,Constants.BASE_IMG_URL + backdropPath, budget = 0, "","",originalLanguage,originalTitle,overview,popularity,Constants.BASE_IMG_URL + posterPath,releaseDate,0,0,
    "","",title,video,voteAverage,voteCount,false)
}

