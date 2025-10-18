package br.edu.scl.sdm.moviesmanager.model.repository

import br.edu.scl.sdm.moviesmanager.model.dao.MovieDao
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao) {

    suspend fun createMovie(movie: Movie): Result<Unit> {
        return try {
            movieDao.createMovie(movie)
            Result.success(Unit)
        } catch (e: Exception) {
            val mensagem = when {
                e.message?.contains("UNIQUE constraint failed", true) == true ->
                    "There is already a movie with that name."
                e.message?.contains("NOT NULL constraint failed", true) == true ->
                    "The name field is required."
                else ->
                    "Error saving movie."
            }
            Result.failure(Exception(mensagem))
        }
    }

    fun getAllMovies(): Flow<List<Movie>> = movieDao.retrieveMovies()

    suspend fun updateMovie(movie: Movie): Result<Unit> {
        return try {
            movieDao.updateMovie(movie)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error updating movie"))
        }
    }


    suspend fun deleteMovie(movie: Movie): Result<Unit> {
        return try {
            movieDao.deleteMovie(movie)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error deleting movie"))
        }
    }
}
