package com.naar.myapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.naar.myapplication.databinding.MovieListItemBinding
import com.naar.myapplication.domain.models.MovieDetails
import com.naar.myapplication.ui.movieslistscreen.MoviesListFragmentDirections

class MovieAdapter : PagingDataAdapter<MovieDetails, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            MovieListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null) {
            holder.bind(item)
        }
    }

    class MovieViewHolder (
        private val binding : MovieListItemBinding
            ): RecyclerView.ViewHolder(binding.root){
                fun bind(item: MovieDetails){
                    Log.d("Binding", "${item.backdrop_path}      ${item.poster_path}")
                    binding.apply {
                        movie = item
                        clickListener = View.OnClickListener {
                            val action = MoviesListFragmentDirections.actionFirstFragmentToMovieDetailFragment(item.id!!)
                            it.findNavController().navigate(action)
                        }
                    }
                }
            }
}

private class MovieDiffCallback: DiffUtil.ItemCallback<MovieDetails>(){
    override fun areItemsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
        return oldItem.id == newItem.id
    }
}