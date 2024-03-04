package com.movieproject.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadState.Loading
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.movieproject.App
import com.movieproject.R
import com.movieproject.databinding.FragmentMainBinding
import com.movieproject.ui.Movie
import com.movieproject.ui.detail.DetailFragment
import com.movieproject.ui.main.UIState.Error
import com.movieproject.ui.main.UIState.Success
import com.movieproject.ui.main.list.LoadMoreAdapter
import com.movieproject.ui.main.list.MoviesAdapter
import com.movieproject.ui.main.list.MoviesAdapter.OnItemClickListener
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.inject(this)

        val viewModel: MainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        val moviesAdapter = MoviesAdapter(mListener = object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                val detailFragment = DetailFragment.newInstance(movie.id)
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragmentContainerView, detailFragment)
                    addToBackStack(DetailFragment.DETAIL_FRAGMENT_NAME)
                }
            }
        })

        binding.errorButton.setOnClickListener {
            viewModel.loadData()
        }


        viewModel.getMovieData().observe(viewLifecycleOwner){
            lifecycleScope.launch {
                when(it){
                    is Error -> TODO()
                    UIState.Loading -> TODO()
                    is Success -> moviesAdapter.submitData(it.data)
                }
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
            adapter = moviesAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        Log.d("sefwf", binding.recyclerView.adapter?.getItemViewType(position).toString())
                        return 1
                    }
                }
            }
        }

        binding.recyclerView.adapter = moviesAdapter.withLoadStateFooter(
            LoadMoreAdapter()
        )


        /*        viewModel.getMovieData().observe(viewLifecycleOwner) { uiState ->
                    binding.progressBar.isVisible = uiState is UIState.Loading
                    binding.errorText.isVisible = uiState is UIState.Error
                    binding.errorButton.isVisible = uiState is UIState.Error

                    if (uiState is UIState.Success<List<Movie>>) {
                        moviesAdapter.updateMovies(uiState.data)
                    } else if (uiState is UIState.Error) {
                        binding.errorText.text = uiState.error.message
                    }
                }*/
    }

//    private fun loadMovies(
//        viewModel: MainViewModel,
//        moviesAdapter: MoviesAdapter
//    ) {
//        lifecycleScope.launch {
//            viewModel.getMovies().collectLatest {
//                moviesAdapter.submitData(it)
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}