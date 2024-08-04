package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.BookmarkRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.service.BookmarkService
import javax.inject.Inject

class BookmarkRemoteDataSourceImpl @Inject constructor(
    private val bookmarkService: BookmarkService
): BookmarkRemoteDataSource {

    override suspend fun addBookmark(
        accessToken: String,
        diaryId: Int
    ): Result<BaseResponse<MessageResponseDTO>> {
        return try {
            val response = bookmarkService.addBookmark(accessToken, diaryId)
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    Result.success(authResponse)
                } else {
                    Result.failure(Exception("Add Bookmark failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Add Bookmark failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBookmark(
        accessToken: String,
        diaryId: Int
    ): Result<BaseResponse<MessageResponseDTO>> {
        return try {
            val response = bookmarkService.deleteBookmark(accessToken, diaryId)
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    Result.success(authResponse)
                } else {
                    Result.failure(Exception("Delete Bookmark failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Delete Bookmark failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}