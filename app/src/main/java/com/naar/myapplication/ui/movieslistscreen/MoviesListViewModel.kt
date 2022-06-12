package com.naar.myapplication.ui.movieslistscreen

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.naar.myapplication.domain.models.MovieDetails
import com.naar.myapplication.domain.usecase.GetMoviesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO add UiState

@HiltViewModel
class MoviesListViewModel @Inject internal  constructor(
    val getMoviesListUseCase: GetMoviesListUseCase
) : ViewModel(){


    /**
     * Stream of the UI state.
     */
    val state : StateFlow<UiState>

    lateinit var networkState : StateFlow<NetworkState>

    /*
    Data flow consumed by the recyclerview
     */
    val pagingData: Flow<PagingData<MovieDetails>>

    /*
    flow of user actions
     */
    val accept: (MoviesUiAction) -> Unit


    init {
        val actionStateFlow = MutableSharedFlow<MoviesUiAction>()

        /*
        Flow for the search actions
         */
        val searches = actionStateFlow
            .filterIsInstance<MoviesUiAction.Search>()
            .distinctUntilChanged()
            .onStart {
                //at launch default result will be shown
                emit(MoviesUiAction.Search(DEFAULT_QUERY))
            }

        /*
        ** Update the data flow whenever a search query is triggered with the new query
         */
        pagingData = searches
            .flatMapLatest {
                getMovies(it.query)
            }
            .cachedIn(viewModelScope)

        /*
        ** Update the UI State
         */
        state = searches.map {
            val isSearching = if(it.query.isEmpty() || it.query.isBlank()) false else true
            UiState(it.query, isSearching)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }


    private fun getMovies(query: String)  = getMoviesListUseCase.getMoviesList(query)
}


data class UiState(
    var query: String = DEFAULT_QUERY,
    var isSearching: Boolean = false
)

data class NetworkState(
    var isConnected: Boolean = false
)

sealed class MoviesUiAction {
    data class Search(var query: String) : MoviesUiAction()
}

private const val DEFAULT_QUERY = ""
