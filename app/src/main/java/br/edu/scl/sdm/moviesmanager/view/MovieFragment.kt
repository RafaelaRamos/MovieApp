package br.edu.scl.sdm.moviesmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.scl.sdm.moviesmanager.databinding.FragmentBinding
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import br.edu.scl.sdm.moviesmanager.view.MainFragment.Companion.EXTRA_MOVIE
import br.edu.scl.sdm.moviesmanager.view.MainFragment.Companion.MOVIE_FRAGMENT_REQUEST_KEY

class MovieFragment : Fragment() {
    private lateinit var fmb: FragmentBinding
    private val navigationArgs: MovieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fmb = FragmentBinding.inflate(inflater, container, false)

        val receivedMovie = navigationArgs.movie
        receivedMovie?.also { movie ->
            with(fmb) {
                nameEt.setText(movie.name)
                yearEt.setText(movie.year)
                studioEt.setText(movie.studio)
                minutesEt.setText(movie.minutes)
                scoreEt.setText(movie.score.toString())
                genreEt.setText(movie.genre)
                navigationArgs.editMovie.also { editMovie ->
                    nameEt.isEnabled = editMovie
                    saveBt.visibility = if (editMovie) VISIBLE else GONE
                }
            }
        }

        fmb.run {
            saveBt.setOnClickListener {
                setFragmentResult(MOVIE_FRAGMENT_REQUEST_KEY, Bundle().apply {
                    val name = nameEt.text.toString()
                    val year = yearEt.text.toString().toIntOrNull() ?: 0
                    val studio = studioEt.text.toString()
                    val minutes = minutesEt.text.toString().toIntOrNull() ?: 0
                    val score = scoreEt.text.toString().toDouble() ?: 0.0
                    val watcher = watchedEt.isChecked
                    val genre = genreEt.text.toString()

                    val movie = Movie(name, year, studio, minutes, watcher, score, genre)
                    putParcelable(
                        EXTRA_MOVIE, movie
                    )
                })
                findNavController().navigateUp()
            }
        }

        return fmb.root
    }
}