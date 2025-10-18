package br.edu.scl.sdm.moviesmanager.view

import MovieAdapter
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.sdm.moviesmanager.R
import br.edu.scl.sdm.moviesmanager.controller.MainController
import br.edu.scl.sdm.moviesmanager.databinding.FragmentMainBinding
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import br.edu.scl.sdm.moviesmanager.view.adapter.OnMovieClickListener

class MainFragment : Fragment(), OnMovieClickListener {

    private lateinit var fmb: FragmentMainBinding
    private val movieList: MutableList<Movie> = mutableListOf()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(movieList, this)
    }

    private val navController by lazy { findNavController() }

    private val mainController: MainController by lazy { MainController(this) }

    companion object {
        const val EXTRA_MOVIE = "EXTRA_MOVIE"
        const val MOVIE_FRAGMENT_REQUEST_KEY = "MOVIE_FRAGMENT_REQUEST_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(MOVIE_FRAGMENT_REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == MOVIE_FRAGMENT_REQUEST_KEY) {
                val movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(EXTRA_MOVIE, Movie::class.java)
                } else {
                    bundle.getParcelable(EXTRA_MOVIE)
                }
                movie?.also { receivedMovie ->
                    movieList.indexOfFirst { it.name == receivedMovie.name }.also { position ->
                        if (position != -1) {
                            mainController.editMovie(receivedMovie)
                            movieList[position] = receivedMovie
                            movieAdapter.notifyItemChanged(position)
                        } else {
                            mainController.insertMovie(receivedMovie)
                            movieList.add(receivedMovie)
                            movieAdapter.notifyItemInserted(movieList.lastIndex)
                        }
                    }
                }

                (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    fmb.root.windowToken,
                    HIDE_NOT_ALWAYS
                )
            }
        }

        mainController.getMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fmb = FragmentMainBinding.inflate(inflater, container, false)

        fmb.movieRv.layoutManager = LinearLayoutManager(requireContext())
        fmb.movieRv.adapter = movieAdapter

        fmb.addMovieFab.setOnClickListener {
            navController.navigate(MainFragmentDirections.actionMainFragmentToMovieFragment(null, false))
        }

        return fmb.root
    }

    override fun onMovieClick(position: Int) {
        val movie = movieList[position]
        navController.navigate(MainFragmentDirections.actionMainFragmentToMovieFragment(movie, false))
    }

    override fun onMovieLongClick(position: Int, view: View) {
        val movie = movieList[position]
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_tile, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.remove -> {
                    mainController.removeMovie(movie)
                    movieList.removeAt(position)
                    movieAdapter.notifyItemRemoved(position)
                    true
                }
                R.id.details -> {
                    navController.navigate(MainFragmentDirections.actionMainFragmentToMovieFragment(movie, true))
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onRemoveMovieMenuItemClick(position: Int) {
        val movie = movieList[position]
        mainController.removeMovie(movie)
        movieList.removeAt(position)
        movieAdapter.notifyItemRemoved(position)
    }
    override fun onEditMovieMenuItemClick(position: Int) {
        val movie = movieList[position]
        val action = MainFragmentDirections.actionMainFragmentToMovieFragment(
            movie = movie,
            editMovie = true
        )

        findNavController().navigate(action)
    }

    fun updateMovieList(movies: List<Movie>) {
        movieList.clear()
        movieList.addAll(movies)
        movieAdapter.notifyDataSetChanged()
    }
}
