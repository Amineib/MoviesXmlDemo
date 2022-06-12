package com.naar.myapplication.domain.models

import com.google.gson.annotations.SerializedName

data class Movie (
    val backdropPath: String?,
    val id: Int?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val voteAverage: Double?
)