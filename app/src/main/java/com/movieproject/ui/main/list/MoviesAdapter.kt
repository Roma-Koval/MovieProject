package com.movieproject.ui.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.movieproject.databinding.ItemLayoutBinding
import com.movieproject.ui.Movie

class MoviesAdapter(
    val mListener: OnItemClickListener
) : PagingDataAdapter<Movie, MovieViewHolder>(MovieDiffCallBack()) {

//    private val movies: MutableList<Movie> = mutableListOf()
//
//    fun updateMovies(movie: List<Movie>) {
//        movies.clear()
//        movies.addAll(movie)
//        notifyDataSetChanged()
//    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding, mListener)
    }

//    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }
}