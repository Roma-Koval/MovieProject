package com.movieproject.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.movieproject.App
import com.movieproject.databinding.FragmentDetailBinding
import com.movieproject.ui.MovieInfo
import com.movieproject.ui.main.UIState
import javax.inject.Inject
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var detailViewModelFactory: DetailViewModelFactory

    private val viewModel: DetailsViewModel by viewModels { detailViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getInt(MOVIE_ID_KEY)

        viewModel.loadMovieInfo(movieId!!)

        binding.detailErrorButton.setOnClickListener {
            viewModel.loadMovieInfo(movieId)
            binding.groupViews.isVisible = true
        }

        binding.detailToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getState().collect { uiState ->
                    binding.detailProgressBar.isVisible = uiState is UIState.Loading
                    binding.detailErrorText.isVisible = uiState is UIState.Error
                    binding.detailErrorButton.isVisible = uiState is UIState.Error

                    if (uiState is UIState.Success<MovieInfo>) {
                        Glide.with(this@DetailsFragment).load(uiState.data.url)
                            .into(binding.poster)
                        Glide.with(this@DetailsFragment).load(uiState.data.url)
                            .into(binding.backgroundPoster)

                        binding.releaseDate.text = uiState.data.releaseDate
                        binding.genres.text = uiState.data.genres.joinToString { it }
                        binding.rating.text = uiState.data.rating.toString()
                        binding.duration.text = uiState.data.duration.toString()
                        binding.overview.text = uiState.data.overview

                        binding.detailToolbar.title = uiState.data.title

                        binding.groupViews.isVisible = true
                    } else if (uiState is UIState.Error) {
                        binding.detailErrorText.text = uiState.error.message
                        binding.groupViews.isVisible = false
                    }
                }
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
            DetailsFragment().apply {
                arguments = bundleOf(
                    MOVIE_ID_KEY to movieId
                )
            }
    }
}