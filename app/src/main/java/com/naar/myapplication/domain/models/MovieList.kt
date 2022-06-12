package com.naar.myapplication.domain.models

data class MovieList (
    val page: Int,
    val results: List<Movie>,
)