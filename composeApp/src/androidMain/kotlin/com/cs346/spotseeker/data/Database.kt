package com.cs346.spotseeker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.cs346.spotseeker.domain.PhotoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Room database for the app. Version 1 initial schema. */
@Database(entities = [PhotoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}

fun getRoomDatabase(
    applicationContext: android.content.Context
): AppDatabase {
    val builder = databaseBuilder<AppDatabase>(
        context = applicationContext,
        name = applicationContext.getDatabasePath("spotseeker.db").toString()
    )

    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}