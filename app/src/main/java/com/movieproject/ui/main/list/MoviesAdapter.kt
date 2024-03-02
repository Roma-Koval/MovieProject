package com.movieproject.ui.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movieproject.databinding.ItemLayoutBinding
import com.movieproject.ui.Movie

class MoviesAdapter(
    val mListener: OnItemClickListener
) : RecyclerView.Adapter<MovieViewHolder>() {

    private val movies: MutableList<Movie> = mutableListOf()

    fun updateMovies(movie: List<Movie>) {
        movies.clear()
        movies.addAll(movie)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding, mListener)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val data = movies[position]
        holder.bind(data)
    }
}