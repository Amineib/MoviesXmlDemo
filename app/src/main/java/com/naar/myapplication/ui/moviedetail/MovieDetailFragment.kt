package com.naar.myapplication.ui.moviedetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.naar.myapplication.R
import com.naar.myapplication.databinding.MovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private val viewModel : MovieDetailsViewModel by viewModels()
    private var _binding: MovieDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                binding.movie = it.movie
                binding.isFavourite = it.isFavourite
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favourite -> {
                addFavourite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        bindFavourite(menu)
    }

    private fun bindFavourite(menu: Menu){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.map {
                it.isFavourite
            }.collect { isFavourite ->
                if(isFavourite){
                    menu.get(1).setIcon(R.drawable.favourite)
                }else{
                    menu.get(1).setIcon(R.drawable.favourite_heart_outline_foreground)
                }
            }
        }
    }

    private fun addFavourite(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.accept(UiAction.AddFavourite)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}