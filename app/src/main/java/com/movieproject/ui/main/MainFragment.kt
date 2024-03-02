package com.movieproject.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.movieproject.App
import com.movieproject.R
import com.movieproject.databinding.FragmentMainBinding
import com.movieproject.ui.Movie
import com.movieproject.ui.detail.DetailFragment
import com.movieproject.ui.main.list.MoviesAdapter
import com.movieproject.ui.main.list.MoviesAdapter.OnItemClickListener
import javax.inject.Inject


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

        binding.errorButton.setOnClickListener { viewModel.loadData() }

        binding.recyclerView.apply {
            adapter = moviesAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
        }

        viewModel.getMovieData().observe(viewLifecycleOwner) { uiState ->
            binding.progressBar.isVisible = uiState is UIState.Loading
            binding.errorText.isVisible = uiState is UIState.Error
            binding.errorButton.isVisible = uiState is UIState.Error

            if (uiState is UIState.Success<List<Movie>>) {
                moviesAdapter.updateMovies(uiState.data)
            } else if (uiState is UIState.Error) {
                binding.errorText.text = uiState.error.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}