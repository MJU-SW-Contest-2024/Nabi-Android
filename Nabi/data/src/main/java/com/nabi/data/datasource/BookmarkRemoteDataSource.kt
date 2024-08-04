package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO

interface BookmarkRemoteDataSource {

    suspend fun addBookmark(accessToken: String, diaryId: Int): Result<BaseResponse<MessageResponseDTO>>

    suspend fun deleteBookmark(accessToken: String, diaryId: Int): Result<BaseResponse<MessageResponseDTO>>
}