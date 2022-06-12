package com.naar.myapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM favourites where movieId = :id")
    fun getFavourite(id: Int): Flow<FavouritesEntity?>

    @Query("SELECT * FROM favourites where movieId = :id")
    suspend fun getById(id: Int): FavouritesEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavouritesEntity)

    @Delete
    suspend fun delete(movie: FavouritesEntity)
}