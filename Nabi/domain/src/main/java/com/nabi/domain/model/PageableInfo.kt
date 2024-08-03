package com.nabi.domain.model

data class PageableInfo(
    val totalPages: Int,
    val totalElements: Int,
    val elementSize: Int,
    val currentPageNumber: Int,
)
