package com.movieproject.data.model

import com.google.gson.annotations.SerializedName

data class MovieInfoModel(
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val url: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("budget")
    val budget: Int,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("runtime")
    val duration: Int,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("vote_count")
    val totalVote: Int,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("overview")
    val overview: String
)
