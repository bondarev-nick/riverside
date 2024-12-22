package com.example.riverside.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdbservice.error.OmdbException
import com.example.riverside.repository.MoviesRepository
import com.example.riverside.repository.MoviesSearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun search(query: String) {
        currentQuery = "$query*" // adding wildcard for better search UX
        searchInternal(query, 1)
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

    companion object {
        private const val ITEM_PER_PAGE = 10
    }
}