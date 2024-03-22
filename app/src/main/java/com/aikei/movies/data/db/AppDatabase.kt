package com.aikei.movies.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aikei.movies.data.db.dao.MoviesDao
import com.aikei.movies.data.db.entities.FavoriteMovie
import com.aikei.movies.data.db.entities.PopularMovie

@Database(entities = [FavoriteMovie::class, PopularMovie::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val dbName = "movies_database"

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    dbName
                )
                    // .fallbackToDestructiveMigration() // Comment out for production
                    .addMigrations(MIGRATION_1_2) // Apply migrations
                    .build()

                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Perform migration tasks here, e.g.:
                // database.execSQL("ALTER TABLE favorite_movies ADD COLUMN new_column INTEGER DEFAULT 0 NOT NULL")
            }
        }
    }
}