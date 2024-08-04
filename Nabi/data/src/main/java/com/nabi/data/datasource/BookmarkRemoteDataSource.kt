package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.auth.NicknameResponseDTO
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.auth.SignInResponseDTO
import com.nabi.data.model.diary.DiaryDetailResponseDTO

interface BookmarkRemoteDataSource {

    suspend fun addBookmark(accessToken: String, diaryId: Int): Result<BaseResponse<MessageResponseDTO>>

    suspend fun deleteBookmark(accessToken: String, diaryId: Int): Result<BaseResponse<MessageResponseDTO>>
}