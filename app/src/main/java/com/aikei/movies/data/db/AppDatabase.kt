package com.aikei.movies.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aikei.movies.data.db.dao.MoviesDao
import com.aikei.movies.data.db.entities.FavoriteMovie

@Database(entities = [FavoriteMovie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorite_movies"
                ).build()
                INSTANCE = instance
                // Return the created database instance
                instance
            }
        }
    }
}