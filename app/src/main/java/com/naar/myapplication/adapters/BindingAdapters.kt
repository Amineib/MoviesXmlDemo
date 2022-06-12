package com.naar.myapplication.adapters

import android.opengl.Visibility
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.naar.myapplication.R


@BindingAdapter("imageFromUrl")
fun ImageView.bindImageFromUrl(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_baseline_search_24)
            .into(this)
    }
}

@BindingAdapter("setVisibilityFromBoolean")
fun setVisibilityFromBoolean(view: View,value: Boolean){
    if(value){
        view.visibility = View.VISIBLE
    }else{
        view.visibility = View.GONE
    }
}




/*
@BindingConversion
fun booleanToVisibility(value: Boolean) = if (value == true)View.VISIBLE else View.GONE
 */
