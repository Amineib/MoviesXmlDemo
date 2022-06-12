package com.naar.myapplication.ui.moviedetail

import androidx.lifecycle.*
import com.naar.myapplication.domain.models.MovieDetails
import com.naar.myapplication.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject internal constructor(
    val useCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val id : Int = savedStateHandle.get<Int>("movieId")!!

    private val favourite  = useCase.getFavourite(id)

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var job : Job? = null

    /*
    flow of user actions
     */
    val accept: (UiAction) -> Unit

    init {
        //update fav button


        val actionStateFlow = MutableSharedFlow<UiAction>()

        actionStateFlow.filterIsInstance<UiAction.AddFavourite>()
            .distinctUntilChanged().map {
                updateFavorite()
            }


        accept = { action ->
            when(action){
                is UiAction.AddFavourite -> {
                    if(_uiState.value.isFavourite == false){
                        updateFavorite()
                    }else{
                        removeFavorite()
                    }

                }
            }
        }

        viewModelScope.launch{
            favourite.collect {
                if(it == null){
                    _uiState.update {
                        it.copy(
                            isFavourite = false
                        )
                    }
                }else{
                    _uiState.update {
                        it.copy(
                            isFavourite = true
                        )
                    }
                }
            }
        }

        loadMovie()
    }


   private fun loadMovie () {
       job?.cancel()
       job = viewModelScope.launch {
            val result = useCase.getMovieDetails(id)
           result.collect { movie ->
               _uiState.update {
                   it.copy(
                       movie
                   )
               }
           }
       }
    }

    private fun updateFavorite(){
        viewModelScope.launch {
            useCase.addFavourite(id)
        }
    }

    private fun removeFavorite(){
        viewModelScope.launch {
            useCase.deleteFavourite(id)
        }
    }
}


data class UiState(
    var movie: MovieDetails? = null,
    var isFavourite: Boolean = false
)


sealed class UiAction {
    object AddFavourite : UiAction()
}

