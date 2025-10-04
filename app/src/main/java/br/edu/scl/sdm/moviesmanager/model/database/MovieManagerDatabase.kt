package br.edu.scl.sdm.moviesmanager.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.scl.sdm.moviesmanager.model.dao.MovieDao
import br.edu.scl.sdm.moviesmanager.model.entity.Movie


@Database(entities = [Movie::class], version = 1)
abstract class MovieManagerDatabase: RoomDatabase() {
    companion object {
        const val MOVIE_DATABASE = "movieDatabase"
    }
    abstract fun getMovieDao(): MovieDao
}