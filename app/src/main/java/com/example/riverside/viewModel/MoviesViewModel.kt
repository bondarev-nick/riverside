package com.example.riverside.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdbservice.error.OmdbException
import com.example.riverside.repository.MoviesRepository
import com.example.riverside.repository.model.DetailsPreview
import com.example.riverside.repository.model.MovieDetails
import com.example.riverside.repository.model.MoviesSearchResult
import com.example.riverside.repository.model.asFull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private var currentQuery = ""

    private val _searchResult: MutableStateFlow<MoviesSearchResult> = MutableStateFlow(
        MoviesSearchResult(
            movieItems = emptyList(),
            totalItems = 0,
            currentPage = 0,
            error = null
        )
    )
    val searchResult = _searchResult.asStateFlow()

    private val _selectedCardIndex = MutableStateFlow<Int?>(null)
    val selectedCardIndex = _selectedCardIndex.asStateFlow()

    // construct preview from search result item for better UX
    private val _detailsPreview = _selectedCardIndex.map { index ->
        buildDetailsPreview(index)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // fetch full details async and replace preview
    private val _detailsFull = _selectedCardIndex.map { index ->
        fetchDetails(index)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val selectedMovie =
        combine(_detailsPreview, _detailsFull) { preview, full ->
            when {
                full != null -> full
                preview != null -> preview.asFull()
                else -> null
            }
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun search(query: String) {
        currentQuery = "$query*" // adding wildcard for better search UX
        searchInternal(query, 1)
    }

    fun selectCard(index: Int) {
        _selectedCardIndex.update { index }
    }

    fun deselectCard() {
        _selectedCardIndex.update { null }
    }

    private fun searchInternal(query: String, page: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                _searchResult.update {
                    moviesRepository.search(query, page)
                }
            }.onFailure { error ->
                when (error) {
                    is OmdbException -> {
                        _searchResult.update {
                            MoviesSearchResult(
                                movieItems = emptyList(),
                                totalItems = 0,
                                currentPage = 0,
                                error = error.message
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchDetails(index: Int?): MovieDetails? {
        return index?.let { indexSafe ->
            _searchResult.value.movieItems.getOrNull(indexSafe)?.imdbID?.let { idSafe ->
                kotlin.runCatching {
                    moviesRepository.getDetails(idSafe)
                }.getOrNull()
            }
        }
    }

    fun next() {
        if (hasNextPage()) {
            searchInternal(currentQuery, _searchResult.value.currentPage + 1)
        }
    }

    fun prev() {
        if (hasPrevPage()) {
            searchInternal(currentQuery, _searchResult.value.currentPage - 1)
        }
    }

    fun hasNextPage(): Boolean {
        return _searchResult.value.currentPage * ITEM_PER_PAGE < _searchResult.value.totalItems
    }

    fun hasPrevPage(): Boolean {
        return _searchResult.value.currentPage > 1
    }

    private fun buildDetailsPreview(index: Int?): DetailsPreview? {
        return index?.let { indexSafe ->
            _searchResult.value.movieItems.getOrNull(indexSafe)?.let { movieItem ->
                DetailsPreview(
                    title = movieItem.title,
                    coverUrl = movieItem.posterUrl
                )
            }
        }
    }

    companion object {
        private const val ITEM_PER_PAGE = 10
    }
}