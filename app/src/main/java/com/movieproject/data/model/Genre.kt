package com.movieproject.data.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("name")
    val genre: String
)
