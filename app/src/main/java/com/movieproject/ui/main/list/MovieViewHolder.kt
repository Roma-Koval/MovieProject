package com.movieproject.ui.main.list

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movieproject.databinding.ItemLayoutBinding
import com.movieproject.ui.Movie

class MovieViewHolder(
    private val itemBinding: ItemLayoutBinding,
    private val listener: MoviesAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(data: Movie) {
        Glide.with(itemBinding.root.context).load(data.url)
            .into(itemBinding.poster)

        itemBinding.title.text = data.title
        itemBinding.date.text = data.releaseDate.substring(0, 4)

        itemBinding.root.setOnClickListener {
            listener.onItemClick(data)
        }
    }
}