package com.naar.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favourites")
data class FavouritesEntity (
    @PrimaryKey var movieId: Int
)