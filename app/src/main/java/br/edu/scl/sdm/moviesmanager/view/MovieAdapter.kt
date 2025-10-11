package br.edu.scl.sdm.moviesmanager.view

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.sdm.moviesmanager.R
import br.edu.scl.sdm.moviesmanager.databinding.TileMovieBinding
import br.edu.scl.sdm.moviesmanager.model.entity.Movie

class MovieAdapter (context: Context, private val movieList: MutableList<Movie>) :
    ArrayAdapter<Movie>(
        context,
        R.layout.tile_movie, movieList
    ) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val movie = movieList[position]

        var MovieTileView = convertView
        if (MovieTileView == null) {
            val tcb = TileMovieBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater, parent, false
            )
            MovieTileView = tcb.root
            val tileMovieHolder = TileMovieHolder(tcb.nameTV, tcb.genreTV)
            MovieTileView.tag = tileMovieHolder
        }
        val holder = MovieTileView.tag as TileMovieHolder
        holder.nameTv.text = movie.name
        holder.email.text = movie.genre

        return MovieTileView
    }

    private data class TileMovieHolder(val nameTv: TextView, val email: TextView)
}