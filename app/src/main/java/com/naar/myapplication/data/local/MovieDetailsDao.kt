package com.naar.myapplication.data.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDetailsDao {

    @Query("SELECT * from MovieDetailsEntity")
    fun loadAllFavourites(): PagingSource<Int,MovieDetailsEntity>

    @Query("SELECT * from MovieDetailsEntity WHERE id = :id")
    fun loadById(id: Int): Flow<MovieDetailsEntity?>


    @Update
    suspend fun update(movie: MovieDetailsEntity)


    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(movie: MovieDetailsEntity)

    @Delete
    suspend fun delete(movie: MovieDetailsEntity)

    @Query("DELETE FROM moviedetailsentity")
    suspend fun clearMovies()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieDetailsEntity>)


}