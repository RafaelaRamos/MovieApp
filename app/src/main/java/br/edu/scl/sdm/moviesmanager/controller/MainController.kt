package br.edu.scl.sdm.moviesmanager.controller

import androidx.room.Room
import br.edu.scl.sdm.moviesmanager.model.database.MovieManagerDatabase
import br.edu.scl.sdm.moviesmanager.model.database.MovieManagerDatabase.Companion.MOVIE_DATABASE
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import br.edu.scl.sdm.moviesmanager.view.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainController(private val mainFragment: MainFragment) {
    private val movieDaoImpl = Room.databaseBuilder(
        mainFragment.requireContext(),
        MovieManagerDatabase::class.java,
        MOVIE_DATABASE
    ).build().getMovieDao()

    fun insertMovie(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDaoImpl.createMovie(movie)
        }
    }

    fun getMovies() {
        CoroutineScope(Dispatchers.IO).launch {
          val movie =  movieDaoImpl.retrieveMovie()
            mainFragment.updateMovieList(movie)
        }
    }

    fun editMovie(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
             movieDaoImpl.updateMovie(movie)
        }
    }

    fun removeMovie(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDaoImpl.deleteMovie(movie)
        }
    }
}