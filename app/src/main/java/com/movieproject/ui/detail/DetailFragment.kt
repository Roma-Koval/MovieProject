package com.movieproject.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.movieproject.App
import com.movieproject.databinding.FragmentDetailBinding
import com.movieproject.ui.MovieInfo
import com.movieproject.ui.main.MainViewModel
import com.movieproject.ui.main.MainViewModelFactory
import com.movieproject.ui.main.UIState
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.inject(this)

        val movieId = arguments?.getInt(MOVIE_ID_KEY)

        val mainViewModel: MainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        mainViewModel.loadMovieInfo(movieId!!)

        binding.detailErrorButton.setOnClickListener {
            mainViewModel.loadMovieInfo(movieId)
        }

        mainViewModel.getMovieInfoData().observe(viewLifecycleOwner) { uiState ->
            binding.detailProgressBar.isVisible = uiState is UIState.Loading
            binding.detailErrorText.isVisible = uiState is UIState.Error
            binding.detailErrorButton.isVisible = uiState is UIState.Error

            if (uiState is UIState.Success<MovieInfo>) {
                Glide.with(this).load(uiState.data.url)
                    .into(binding.poster)

                val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
                binding.budget.text = formatCurrency.format(uiState.data.budget)
                binding.revenue.text = formatCurrency.format(uiState.data.revenue)

                binding.detailTitle.text = uiState.data.title
                binding.releaseDate.text = uiState.data.releaseDate
                binding.genres.text = uiState.data.genres.joinToString { it }
                binding.rating.text = uiState.data.rating.toString()
                binding.totalVotes.text = uiState.data.totalVote.toString()
                binding.duration.text = uiState.data.duration.toString()
                binding.overview.text = uiState.data.overview

            } else if (uiState is UIState.Error) {
                binding.detailErrorText.text = uiState.error.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MOVIE_ID_KEY = "id"
        const val DETAIL_FRAGMENT_NAME = "DetailFragment"

        fun newInstance(movieId: Int) =
            DetailFragment().apply {
                arguments = bundleOf(
                    MOVIE_ID_KEY to movieId
                )
            }
    }
}