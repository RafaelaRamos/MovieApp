package br.edu.scl.sdm.moviesmanager.view.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
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

        var movieTileView = convertView
        if (movieTileView == null) {
            val tcb = TileMovieBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater, parent, false
            )
            movieTileView = tcb.root
            val tileMovieHolder = TileMovieHolder(tcb.nameTV, tcb.watchedIv)
            movieTileView.tag = tileMovieHolder
        }
        val holder = movieTileView.tag as TileMovieHolder
        holder.nameTv.text = movie.name

        val watchedIcon = movieTileView.findViewById<ImageView>(R.id.watchedIv)
        val statusText = movieTileView.findViewById<TextView>(R.id.statusTV)

        if (movie.watched) {
            watchedIcon.setImageResource(R.drawable.ic_check_foreground)
            watchedIcon.setColorFilter(Color.parseColor("#4CAF50"))
            statusText.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            watchedIcon.setImageResource(R.drawable.ic_close_foreground)
            watchedIcon.setColorFilter(Color.parseColor("#F44336"))
            statusText.text = context.getString(R.string.unwatched)
            statusText.setTextColor(Color.parseColor("#F44336"))
        }

        return movieTileView
    }

    private data class TileMovieHolder(val nameTv: TextView, val watchedIv:ImageView)
}