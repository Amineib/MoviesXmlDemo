package com.naar.myapplication.data.movies

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.naar.myapplication.api.TmdbService
import com.naar.myapplication.data.local.FavouriteMoviesDatabase
import com.naar.myapplication.data.local.MovieDetailsEntity
import com.naar.myapplication.data.local.RemoteKeys
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val query: String,
    private val service: TmdbService,
    private val database: FavouriteMoviesDatabase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteMediator<Int, MovieDetailsEntity>(){

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieDetailsEntity>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
               val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.preKey
                if(prevKey == null){
                    return MediatorResult.Success(remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                val nextKeys = remoteKeys?.nextKey
                if(nextKeys == null){
                    return MediatorResult.Success(remoteKeys != null)
                }
                nextKeys
            }
        }

        return withContext(defaultDispatcher){
            try {
                val response =
                    if(query != ""){
                        service.searchMovie(query, page)
                    }else{
                        service.getMoviesList(page)
                    }
            val movies = response.results.map {
                it.toDatabaseEntity()
            }
            val endOfPaginationReached = movies.isEmpty()

            database.withTransaction {
                //clear all tables
                if(loadType == LoadType.REFRESH){
                    database.remoteKeysDao().clearRemoteKeys()
                    database.favouriteMoviesDao().clearMovies()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys  = movies.map {
                    RemoteKeys(
                        it.id, prevKey, nextKey
                    )
                }
                database.remoteKeysDao().insertAll(keys)
                database.favouriteMoviesDao().insertAll(movies)
                MediatorResult.Success(endOfPaginationReached)
            }
            }catch (e: Exception){
                MediatorResult.Error(e)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieDetailsEntity>) : RemoteKeys? {
        return state.pages.lastOrNull() {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            database.remoteKeysDao().remoteKeysRepoId(it.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieDetailsEntity>) : RemoteKeys? {
        return state.pages.firstOrNull() {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            database.remoteKeysDao().remoteKeysRepoId(it.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieDetailsEntity>) : RemoteKeys? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let {
                database.remoteKeysDao().remoteKeysRepoId(it)
            }
        }
    }
}