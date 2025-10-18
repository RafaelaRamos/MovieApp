package br.edu.scl.sdm.moviesmanager.view

import MovieAdapter
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.sdm.moviesmanager.R
import br.edu.scl.sdm.moviesmanager.databinding.FragmentMainBinding
import br.edu.scl.sdm.moviesmanager.model.database.MovieManagerDatabase
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import br.edu.scl.sdm.moviesmanager.model.repository.MovieRepository
import br.edu.scl.sdm.moviesmanager.view.adapter.OnMovieClickListener
import br.edu.scl.sdm.moviesmanager.viewModel.MovieViewModel
import br.edu.scl.sdm.moviesmanager.viewModel.MovieViewModelFactory


class MainFragment : Fragment(), OnMovieClickListener {

    private lateinit var fmb: FragmentMainBinding
    private val movieList: MutableList<Movie> = mutableListOf()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel
    private val navController by lazy { findNavController() }

    companion object {
        const val EXTRA_MOVIE = "EXTRA_MOVIE"
        const val MOVIE_FRAGMENT_REQUEST_KEY = "MOVIE_FRAGMENT_REQUEST_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MovieRepository(MovieManagerDatabase.getDatabase(requireContext()).getMovieDao())
        val factory = MovieViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]


        viewModel.movies.observe(this) { movies ->
            movieList.clear()
            movieList.addAll(movies)
            movieAdapter.notifyDataSetChanged()
        }

        viewModel.message.observe(this) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }

        setFragmentResultListener(MOVIE_FRAGMENT_REQUEST_KEY) { _, bundle ->
            val movie = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(EXTRA_MOVIE, Movie::class.java)
            } else {
                bundle.getParcelable(EXTRA_MOVIE)
            }

            movie?.let { receivedMovie ->
                if (receivedMovie.name.isNotEmpty() && movieList.any { it.name == receivedMovie.name }) {
                    viewModel.updateMovie(receivedMovie)
                } else {
                    viewModel.addMovie(receivedMovie)
                }

                (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(fmb.root.windowToken, HIDE_NOT_ALWAYS)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fmb = FragmentMainBinding.inflate(inflater, container, false)

        movieAdapter = MovieAdapter(movieList, this)
        fmb.movieRv.layoutManager = LinearLayoutManager(requireContext())
        fmb.movieRv.adapter = movieAdapter

        fmb.addMovieFab.setOnClickListener {
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToMovieFragment(
                    movie = null,
                    editMovie = true
                )
            )
        }

        return fmb.root
    }

    override fun onMovieClick(position: Int, view: View) {
        val movie = movieList[position]
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_tile, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.remove -> {
                    viewModel.removeMovie(movie)
                    true
                }
                R.id.details -> {
                    navController.navigate(
                        MainFragmentDirections.actionMainFragmentToMovieFragment(
                            movie = movie,
                            editMovie = false
                        )
                    )
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onRemoveMovieMenuItemClick(position: Int) {
        val movie = movieList[position]
        viewModel.removeMovie(movie)
    }

    override fun onEditMovieMenuItemClick(position: Int) {
        val movie = movieList[position]
        navController.navigate(
            MainFragmentDirections.actionMainFragmentToMovieFragment(
                movie = movie,
                editMovie = true
            )
        )
    }
}
