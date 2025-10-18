package br.edu.scl.sdm.moviesmanager.view.adapter

import android.view.View

interface OnMovieClickListener {

    fun onMovieClick(position: Int, view: View)
    fun onRemoveMovieMenuItemClick(position: Int)
    fun onEditMovieMenuItemClick(position: Int)

}