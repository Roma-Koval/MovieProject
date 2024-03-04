package com.movieproject.ui.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.movieproject.databinding.LoadMoreBinding

class LoadMoreAdapter : LoadStateAdapter<LoadMoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadMoreViewHolder {
        val loadMoreBinding =
            LoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadMoreViewHolder(loadMoreBinding)
    }

    override fun onBindViewHolder(holder: LoadMoreViewHolder, loadState: LoadState) {
        holder.setData(loadState)
    }

    override fun getStateViewType(loadState: LoadState): Int = MOVIES_PAGING_STATE_VIEW_TYPE
}