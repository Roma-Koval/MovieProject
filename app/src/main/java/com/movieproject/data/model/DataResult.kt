package com.movieproject.data.model

import com.google.gson.annotations.SerializedName

data class DataResult<T>(
    @SerializedName("results")
    val result: List<T>
)
