import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.sdm.moviesmanager.R
import br.edu.scl.sdm.moviesmanager.databinding.TileMovieBinding
import br.edu.scl.sdm.moviesmanager.model.entity.Movie
import br.edu.scl.sdm.moviesmanager.view.adapter.OnMovieClickListener

class MovieAdapter(
    private val movies: MutableList<Movie>,
    private val listener: OnMovieClickListener
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: TileMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        init {


            binding.root.setOnClickListener {
                val movie = movies[adapterPosition]
                val popup = PopupMenu(binding.root.context, binding.root)
                popup.menuInflater.inflate(R.menu.menu_tile, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveMovieMenuItemClick(adapterPosition)
                            true
                        }
                        R.id.details -> {
                            listener.onMovieClick(adapterPosition, binding.root)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = TileMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.nameTV.text = movie.name
        val statusText = holder.binding.statusTV
        val watchedIcon = holder.binding.watchedIv

        if (movie.watched) {
            watchedIcon.setImageResource(R.drawable.ic_check_foreground)
            watchedIcon.setColorFilter(Color.parseColor("#4CAF50"))
            statusText.text = holder.binding.root.context.getString(R.string.watched)
            statusText.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            watchedIcon.setImageResource(R.drawable.ic_close_foreground)
            watchedIcon.setColorFilter(Color.parseColor("#F44336"))
            statusText.text = holder.binding.root.context.getString(R.string.unwatched)
            statusText.setTextColor(Color.parseColor("#F44336"))
        }
    }

    override fun getItemCount() = movies.size
}
