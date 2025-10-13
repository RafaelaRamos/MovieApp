package br.edu.scl.sdm.moviesmanager.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import br.edu.scl.sdm.moviesmanager.controller.MainController
import br.edu.scl.sdm.moviesmanager.databinding.FragmentMainBinding
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import br.edu.scl.sdm.moviesmanager.view.adapter.MovieAdapter
import br.edu.scl.sdm.moviesmanager.view.adapter.OnMovieClickListener

class MainFragment : Fragment(), OnMovieClickListener {


    private lateinit var fmb: FragmentMainBinding


    private val movieList: MutableList<Movie> = mutableListOf()
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(requireContext(), movieList = movieList)
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    companion object {
        const val EXTRA_MOVIE = "EXTRA_MOVIE"
        const val MOVIE_FRAGMENT_REQUEST_KEY = "MOVIE_FRAGMENT_REQUEST_KEY"
    }

    //Controller
    private val mainController: MainController by lazy {
        MainController(this)
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
                            movieAdapter.notifyDataSetChanged()
                        } else {
                            mainController.insertMovie(receivedMovie)
                            movieList.add(receivedMovie)
                            movieAdapter.notifyDataSetChanged()
                        }
                    }
                }

                // Hiding soft keyboard
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
        fmb.movieLv.adapter = movieAdapter

        fmb.addMovieFab.setOnClickListener {
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToMovieFragment(null, editMovie = false)
            )
        }
        fmb.movieLv.setOnItemClickListener { _, _, position, _ ->
            val selectedMovie = movieList[position]
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToMovieFragment(
                    selectedMovie,
                    editMovie = true
                )
            )
        }

        return fmb.root
    }


    private fun navigateToMovieFragment(position: Int, editTask: Boolean) {
        movieList[position].also {
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToMovieFragment(it, editTask)
            )
        }
    }

    fun updateMovieList(movie: List<Movie>) {
        movieList.clear()
        movieList.addAll(movie)
        movieAdapter.notifyDataSetChanged() // <
    }

    override fun onMovieClick(position: Int) = navigateToMovieFragment(position, false)

    override fun onRemoveMovieMenuItemClick(position: Int) {
        mainController.removeMovie(movieList[position])
        movieList.removeAt(position)
        movieAdapter.notifyDataSetChanged()
    }

    override fun onEditMovieMenuItemClick(position: Int) = navigateToMovieFragment(position, true)

}