package com.naar.myapplication.data.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.naar.myapplication.api.TmdbService
import com.naar.myapplication.domain.models.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

private const val STARTING_PAGE_INDEX = 1

class MoviesPagingSource(
    private val service : TmdbService,
    private val query: String,
    private val defaultDispatcher: CoroutineDispatcher
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            withContext(defaultDispatcher){
                val response =
                    if(query != ""){
                        service.searchMovie(query, page)
                    }else{
                        service.getMoviesList(page)
                    }
                val movies = response.toDomain().results
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if(page == response.totalPages) null else page + 1
                )
            }
        }
        catch(e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

}