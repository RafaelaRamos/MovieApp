package br.edu.scl.sdm.moviesmanager.viewModel

import androidx.lifecycle.*
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import br.edu.scl.sdm.moviesmanager.model.repository.MovieRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>(emptyList())
    val movies: LiveData<List<Movie>> get() = _movies
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            repository.getAllMovies().collectLatest { list ->
                _movies.value = list
            }
        }
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            val result = repository.createMovie(movie)
            result.onFailure { throwable ->
                _message.value = throwable.message ?: "Error adding movie"
            }
            result.onSuccess {
                _message.value = "Movie added successfully"
            }
        }
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch {
            val result = repository.updateMovie(movie)
            result.onFailure { throwable ->
                _message.value = throwable.message ?: "Error updating movie"
            }
            result.onSuccess {
                _message.value = "Movie updated successfully"
            }
        }
    }

    fun removeMovie(movie: Movie) {
        viewModelScope.launch {
            val result = repository.deleteMovie(movie)
            result.onFailure { throwable ->
                _message.value = throwable.message ?: "Error deleting movie"
            }
            result.onSuccess {
                _message.value = "Movie removed successfully"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
