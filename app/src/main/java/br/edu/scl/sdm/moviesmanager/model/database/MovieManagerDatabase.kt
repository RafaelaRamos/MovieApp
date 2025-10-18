package br.edu.scl.sdm.moviesmanager.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.scl.sdm.moviesmanager.model.dao.MovieDao
import br.edu.scl.sdm.moviesmanager.model.entity.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieManagerDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieManagerDatabase? = null

        const val MOVIE_DATABASE = "movieDatabase"

        fun getDatabase(context: Context): MovieManagerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieManagerDatabase::class.java,
                    MOVIE_DATABASE
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
