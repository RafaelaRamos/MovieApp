package br.edu.scl.sdm.moviesmanager.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    companion object {
        const val MOVIE_TABLE = "movie"
    }
    @Insert
    suspend fun createMovie(movie: Movie)

    @Query("SELECT * FROM movie")
    fun retrieveMovies(): Flow<List<Movie>>

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)
}
