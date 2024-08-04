package com.nabi.domain.repository

interface BookmarkRepository {

    suspend fun addBookmark(accessToken: String, diaryId: Int): Result<String>

    suspend fun deleteBookmark(accessToken: String, diaryId: Int): Result<String>
}