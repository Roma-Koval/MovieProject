package com.movieproject.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadState.Loading
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.movieproject.App
import com.movieproject.R
import com.movieproject.databinding.FragmentMainBinding
import com.movieproject.ui.Movie
import com.movieproject.ui.detail.DetailsFragment
import com.movieproject.ui.main.list.LoadMoreAdapter
import com.movieproject.ui.main.list.MOVIES_PAGING_STATE_VIEW_TYPE
import com.movieproject.ui.main.list.MOVIE_ITEM_VIEW_TYPE
import com.movieproject.ui.main.list.MoviesAdapter
import com.movieproject.ui.main.list.MoviesAdapter.OnItemClickListener
import javax.inject.Inject
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moviesAdapter = MoviesAdapter(mListener = object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                val detailsFragment = DetailsFragment.newInstance(movie.id)
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragmentContainerView, detailsFragment)
                    addToBackStack(DetailsFragment.DETAIL_FRAGMENT_NAME)
                }
            }
        })

        binding.errorButton.setOnClickListener {
            viewModel.loadMovieData()
        }

        viewModel.getMoviePagingDataState().observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                moviesAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            moviesAdapter.loadStateFlow.collect {
                val state = it.refresh
                binding.progressBar.isVisible = state is Loading
                binding.errorButton.isVisible = state is LoadState.Error
                binding.errorText.isVisible = state is LoadState.Error

                if (state is LoadState.Error) {
                    binding.errorText.text = state.error.message
                }
            }
        }

        binding.recyclerView.apply {
            val concatAdapter = moviesAdapter.withLoadStateFooter(
                LoadMoreAdapter()
            )
            binding.recyclerView.adapter = concatAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (concatAdapter.getItemViewType(position)) {
                            MOVIE_ITEM_VIEW_TYPE -> 1
                            MOVIES_PAGING_STATE_VIEW_TYPE -> 3
                            else -> 1
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}