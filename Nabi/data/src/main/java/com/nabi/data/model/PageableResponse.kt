package com.nabi.data.model

import com.google.gson.annotations.SerializedName

data class PageableResponse<T>(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("content") val content: List<T>,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean,
    @SerializedName("empty") val empty: Boolean
)

data class Sort(
    @SerializedName("empty") val empty: Boolean,
    @SerializedName("sorted") val sorted: Boolean,
    @SerializedName("unsorted") val unsorted: Boolean
)

data class Pageable(
    @SerializedName("pageNumber") val pageNumber: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("offset") val offset: Int,
    @SerializedName("paged") val paged: Boolean,
    @SerializedName("unpaged") val unPaged: Boolean
)
