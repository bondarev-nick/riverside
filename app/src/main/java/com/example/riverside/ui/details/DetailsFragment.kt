package com.example.riverside.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.riverside.databinding.FragmentDetailsBinding
import com.example.riverside.repository.model.MovieDetails
import com.example.riverside.viewModel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val activityViewModel: MoviesViewModel by activityViewModels()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    init {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                activityViewModel.selectedMovie.collect { movieDetails ->
                    bind(movieDetails)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind(details: MovieDetails?) {
        binding.textTitle.text = details?.title.orEmpty()
        binding.textYear.text = details?.year.orEmpty()
        binding.textGenre.text = details?.genre.orEmpty()
        binding.textRatings.text = details?.ratings.orEmpty()
        details?.coverUrl?.let { url ->
            Glide.with(binding.imagePoster.context)
                .load(url)
                .into(binding.imagePoster)
        }
    }
}