package com.movieproject.data.model

import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val url: String,
    @SerializedName("release_date")
    val releaseDate: String
)
