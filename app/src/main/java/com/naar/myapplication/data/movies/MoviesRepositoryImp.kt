package com.naar.myapplication.data.movies

import androidx.paging.*
import com.naar.myapplication.api.TmdbService
import com.naar.myapplication.data.local.FavouriteMoviesDatabase
import com.naar.myapplication.data.local.FavouritesEntity
import com.naar.myapplication.data.local.toDomain
import com.naar.myapplication.domain.models.MovieDetails
import com.naar.myapplication.domain.models.toDatabaseEntity
import com.naar.myapplication.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MoviesRepositoryImp @Inject constructor(
    private val service : TmdbService,
    private val database: FavouriteMoviesDatabase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMoviesList(query: String) : Flow<PagingData<MovieDetails>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = MoviesRemoteMediator(
                query = query,
                service = service,
                database = database
            ),
            pagingSourceFactory = {
                database.favouriteMoviesDao().loadAllFavourites()
            }
        ).flow.map {
            it.map {
                it.toDomain()
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun searchMovie(query: String): Flow<PagingData<MovieDetails>>{
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = MoviesRemoteMediator(
                query = query,
                service = service,
                database = database
            ),
            pagingSourceFactory = {
                database.favouriteMoviesDao().loadAllFavourites()
            }
        ).flow.map {
            it.map {
                it.toDomain()
            }
        }
    }

    override fun getMovieDetails(id: Int) : Flow<MovieDetails?> {
        return database.favouriteMoviesDao().loadById(id).map {
            it?.toDomain()
        }
    }

    override suspend fun updateMovie(movie: MovieDetails) {
        withContext(defaultDispatcher){
            database.favouriteMoviesDao().update(movie.toDatabaseEntity())
        }
    }

    override suspend fun insertFavourite(id: Int) {
        withContext(defaultDispatcher){
            database.favouritesListDao().insert(FavouritesEntity(id))
        }
    }

    override suspend fun removeFavourite(id: Int) {
        withContext(defaultDispatcher){
            val movie = database.favouritesListDao().getById(id)
            database.favouritesListDao().delete(movie)
        }
    }

    override fun getFavourite(id: Int): Flow<FavouritesEntity?> {
        return database.favouritesListDao().getFavourite(id)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }
}