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
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movies_database" // Changed to a more general name
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new version of the table
                database.execSQL("CREATE TABLE IF NOT EXISTS `popular_movies_new` " +
                        "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`title` TEXT NOT NULL, " +
                        "`posterPath` TEXT NOT NULL, " +
                        "`releaseDate` TEXT NOT NULL, " +
                        "`rating` REAL NOT NULL, " +
                        "`overview` TEXT NOT NULL)") // Add the missing column 'overview'

                // Copy the data from the old table to the new one
                database.execSQL("INSERT INTO `popular_movies_new` (`id`, `title`, `posterPath`, `releaseDate`, `rating`, `overview`) " +
                        "SELECT `id`, `title`, `posterPath`, `releaseDate`, `rating`, '' FROM `popular_movies`") // Add default value for 'overview'

                // Remove the old table
                database.execSQL("DROP TABLE `popular_movies`")

                // Rename the new table to the original name
                database.execSQL("ALTER TABLE `popular_movies_new` RENAME TO `popular_movies`")
            }
        }


    }
}
