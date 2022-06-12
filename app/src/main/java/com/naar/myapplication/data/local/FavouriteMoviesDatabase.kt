package com.naar.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [MovieDetailsEntity::class, RemoteKeys::class, FavouritesEntity::class], version = 1,
    exportSchema = true
)
abstract class FavouriteMoviesDatabase : RoomDatabase(){

    abstract fun favouriteMoviesDao(): MovieDetailsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun favouritesListDao(): FavouritesDao

    companion object {

        @Volatile private var instance : FavouriteMoviesDatabase? = null

        fun getInstance(context: Context): FavouriteMoviesDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDataBase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDataBase(context: Context): FavouriteMoviesDatabase{
            return Room.databaseBuilder(context, FavouriteMoviesDatabase::class.java,"fav_movie")
                .build()
        }

    }
}