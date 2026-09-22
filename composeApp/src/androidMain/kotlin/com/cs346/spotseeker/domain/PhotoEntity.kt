package com.cs346.spotseeker.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Room entity representing a Photo in the local SQLite database */
@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "photo_path")
    val photoPath: String,

    @ColumnInfo(name = "x")
    val x: Double,

    @ColumnInfo(name = "y")
    val y: Double,

    @ColumnInfo(name = "good")
    val good: Int = 0,

    @ColumnInfo(name = "bad")
    val bad: Int = 0,
)