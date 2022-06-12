package com.naar.myapplication.api

import com.naar.myapplication.data.movies.MovieDetailResponse
import com.naar.myapplication.data.movies.MovieListResponse
import com.naar.myapplication.util.ApiKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.Flow

interface TmdbService {

    @GET("movie/popular")
    suspend fun getMoviesList(
        @Query("page") page : Int
    ): MovieListResponse

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: Int): MovieDetailResponse

    //Search for a movie
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int
    ) : MovieListResponse


    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create() : TmdbService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger) //log the queries sent to the server
                .addInterceptor(ApiKeyInterceptor()) //Inject API key in the header
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TmdbService::class.java)
        }
    }
}