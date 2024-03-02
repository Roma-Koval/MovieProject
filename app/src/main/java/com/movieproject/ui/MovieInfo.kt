package com.movieproject.ui

data class MovieInfo(
    val title: String,
    val url: String,
    val releaseDate: String,
    val budget: Int,
    val revenue: Int,
    val duration: Int,
    val rating: Double,
    val totalVote: Int,
    val genres: List<String>,
    val overview: String
)
