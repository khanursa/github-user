package com.reston.githubuser.model

import com.google.gson.annotations.SerializedName

data class SearchBase(
    @SerializedName("total_count")
    val totalCount: Long? = null,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    val items: ArrayList<Items>? = null
)
