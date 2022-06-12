package com.naar.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys (
    @PrimaryKey
    val movieId: Int,
    val preKey: Int?,
    val nextKey: Int?
        )