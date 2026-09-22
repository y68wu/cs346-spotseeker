package com.cs346.spotseeker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cs346.spotseeker.domain.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao // Data Access Object for PhotoEntity
interface PhotoDao {
    @Query("SELECT * FROM photos ORDER BY id ASC")
    fun getAll(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE id = :id LIMIT 1")
    fun findById(id: String): PhotoEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(photo: PhotoEntity)

    @Update
    suspend fun update(photo: PhotoEntity)

    @Query("DELETE FROM photos WHERE id = :id")
    suspend fun deleteById(id: String)
}