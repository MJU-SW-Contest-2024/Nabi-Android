package com.nabi.data.repository

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.datasource.BookmarkRemoteDataSource
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.BookmarkRepository
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkRemoteDataSource: BookmarkRemoteDataSource
): BookmarkRepository {

    override suspend fun addBookmark(accessToken: String, diaryId: Int): Result<String> {
        val result = bookmarkRemoteDataSource.addBookmark(accessToken, diaryId)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(data.message)
                } else {
                    Result.failure(Exception("Add Bookmark Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Add Bookmark Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun deleteBookmark(accessToken: String, diaryId: Int): Result<String> {
        val result = bookmarkRemoteDataSource.deleteBookmark(accessToken, diaryId)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(data.message)
                } else {
                    Result.failure(Exception("Delete Bookmark Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Delete Bookmark Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}