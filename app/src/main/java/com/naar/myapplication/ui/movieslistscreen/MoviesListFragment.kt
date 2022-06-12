package com.naar.myapplication.ui.movieslistscreen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenStarted
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.naar.myapplication.adapters.MovieAdapter
import com.naar.myapplication.adapters.LoadStateAdapter
import com.naar.myapplication.data.local.MovieDetailsEntity
import com.naar.myapplication.databinding.MoviesListBinding
import com.naar.myapplication.domain.models.Movie
import com.naar.myapplication.domain.models.MovieDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var _binding: MoviesListBinding? = null
    private val viewModel : MoviesListViewModel by viewModels()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindState(
            viewModel.state,
            viewModel.pagingData,
            viewModel.accept
        )
    }

    private fun MoviesListBinding.bindState(
        uiState : StateFlow<UiState>,
        pagingData : Flow<PagingData<MovieDetails>>,
        uiActions : (MoviesUiAction) -> Unit
    ){
        bindSearch(uiState, onQueryChanged = uiActions)
        bindList(
            pagingData = pagingData
        )
    }

    private fun MoviesListBinding.bindList(pagingData: Flow<PagingData<MovieDetails>>) {
        val adapter = MovieAdapter()

        binding.retryButton.setOnClickListener{
            adapter.retry()
        }

        recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            footer = LoadStateAdapter{ adapter.retry() },
            header = LoadStateAdapter{ adapter.retry() }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            pagingData.collect {
                adapter.submitData(it)
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                progressBar.isVisible = loadStates.refresh is LoadState.Loading

                if(loadStates.mediator!!.refresh is LoadState.Error){
                    val errState = loadStates.mediator!!.refresh as? LoadState.Error
                    errState?.let {
                        showToast(it.error.message.toString())
                        //Log.d("ERRORSTATE", it.error.message.toString())
                    }
                }
            }
        }

    }

    private fun MoviesListBinding.bindSearch(
        uiState : StateFlow<UiState>,
        onQueryChanged : (MoviesUiAction.Search) -> Unit
    ){
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.collect {
                clearBtn.isVisible = it.isSearching
            }
        }

        clearBtn.setOnClickListener {
            clearSearch()
            updateFromInput(onQueryChanged)
        }

        //updateFromInput to notify the viewModel that a search have been executed
        searchTxt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        searchTxt.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        //Get the initial value of the default query to show in the search input
        viewLifecycleOwner.lifecycleScope.launch {
            uiState.map {
                it.query
            }
                .distinctUntilChanged()
                .collect {
                    searchTxt.setText(it)
                }
        }
    }

    private fun MoviesListBinding.clearSearch(){
        searchTxt.setText("")
    }

    private fun MoviesListBinding.updateFromInput(
        onQueryChanged: (MoviesUiAction.Search) -> Unit
    ){
        searchTxt.text.toString().trim().let {
            onQueryChanged(MoviesUiAction.Search(it))
        }
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}