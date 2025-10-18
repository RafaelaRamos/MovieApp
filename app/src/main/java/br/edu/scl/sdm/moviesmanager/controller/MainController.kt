package br.edu.scl.sdm.moviesmanager.controller

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import br.edu.scl.sdm.moviesmanager.model.database.MovieManagerDatabase
import br.edu.scl.sdm.moviesmanager.model.database.MovieManagerDatabase.Companion.MOVIE_DATABASE
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import br.edu.scl.sdm.moviesmanager.model.repository.MovieRepository
import br.edu.scl.sdm.moviesmanager.view.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainController(private val mainFragment: MainFragment) {

    private val movieDaoImpl = Room.databaseBuilder(
        mainFragment.requireContext(),
        MovieManagerDatabase::class.java,
        MOVIE_DATABASE
    ).build().getMovieDao()

    private val movieRepository = MovieRepository(movieDaoImpl)

    fun getMovies() {
        mainFragment.lifecycleScope.launch {
            try {
                movieRepository.getAllMovies()
                    .collect { movies ->
                        mainFragment.updateMovieList(movies)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertMovie(movie: Movie) {
        mainFragment.lifecycleScope.launch {
            val resultado = movieRepository.createMovie(movie)
            if (resultado.isSuccess) {
                Toast.makeText(mainFragment.requireContext(), "Filme salvo com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mainFragment.requireContext(), resultado.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun editMovie(movie: Movie) {
        mainFragment.lifecycleScope.launch {
            val resultado = movieRepository.updateMovie(movie)
            if (resultado.isSuccess) {
                Toast.makeText(mainFragment.requireContext(), "Filme atualizado!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mainFragment.requireContext(), resultado.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun removeMovie(movie: Movie) {
        mainFragment.lifecycleScope.launch {
            val resultado = movieRepository.deleteMovie(movie)
            if (resultado.isSuccess) {
                Toast.makeText(mainFragment.requireContext(), "Filme removido!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mainFragment.requireContext(), resultado.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
