package com.movieproject.ui.main.list

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadState.Loading
import androidx.recyclerview.widget.RecyclerView
import com.movieproject.databinding.LoadMoreBinding

class LoadMoreViewHolder(
    private val binding: LoadMoreBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun setData(state: LoadState) {
        binding.footerProgressBar.isVisible = state is Loading
    }
}