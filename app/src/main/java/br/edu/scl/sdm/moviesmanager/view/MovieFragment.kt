package br.edu.scl.sdm.moviesmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.scl.sdm.moviesmanager.R
import br.edu.scl.sdm.moviesmanager.databinding.FragmentBinding
import br.edu.scl.sdm.moviesmanager.model.entity.Movie

class MovieFragment : Fragment() {

    private lateinit var fmb: FragmentBinding
    private val navigationArgs: MovieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fmb = FragmentBinding.inflate(inflater, container, false)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genre,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fmb.genreEt.adapter = adapter

        val receivedMovie = navigationArgs.movie
        val isEditMode = navigationArgs.editMovie

        if (receivedMovie != null) {
            with(fmb) {
                nameEt.setText(receivedMovie.name)
                yearEt.setText(receivedMovie.year.toString())
                studioEt.setText(receivedMovie.studio)
                minutesEt.setText(receivedMovie.minutes.toString())
                scoreRb.rating = receivedMovie.score?.toFloat() ?: 0f
                watchedEt.isChecked = receivedMovie.watched

                val position = adapter.getPosition(receivedMovie.genre)
                genreEt.setSelection(position)
            }
        }


        with(fmb) {
            nameEt.isEnabled = receivedMovie == null
            setFieldsEnabled(isEditMode)
            saveBt.visibility = VISIBLE
            editBt.visibility = if (isEditMode || receivedMovie == null) GONE else VISIBLE
        }

        fmb.editBt.setOnClickListener {
            fmb.editBt.visibility = GONE
            fmb.saveBt.visibility = VISIBLE
            setFieldsEnabled(true)
        }

        fmb.saveBt.setOnClickListener {
            val genreSelection = fmb.genreEt.selectedItem.toString()
            if (genreSelection == getString(R.string.genre)) {
                Toast.makeText(requireContext(), "Select a valid genre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = fmb.nameEt.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(),
                    getString(R.string.require_movie_name), Toast.LENGTH_SHORT).show()
                fmb.nameEt.requestFocus()
                return@setOnClickListener
            }

            val movieUpdated = Movie(
                name = name,
                year = fmb.yearEt.text.toString().toIntOrNull() ?: 0,
                studio = fmb.studioEt.text.toString(),
                minutes = fmb.minutesEt.text.toString().toIntOrNull() ?: 0,
                watched = fmb.watchedEt.isChecked,
                score = fmb.scoreRb.rating.toDouble(),
                genre = genreSelection
            )

            setFragmentResult(MainFragment.MOVIE_FRAGMENT_REQUEST_KEY, Bundle().apply {
                putParcelable(MainFragment.EXTRA_MOVIE, movieUpdated)
            })

            findNavController().navigateUp()
        }

        return fmb.root
    }

    private fun setFieldsEnabled(enabled: Boolean) {
        with(fmb) {
            listOf(yearEt, studioEt, minutesEt, scoreRb, genreEt, watchedEt).forEach {
                it.isEnabled = enabled
            }
        }
    }
}
