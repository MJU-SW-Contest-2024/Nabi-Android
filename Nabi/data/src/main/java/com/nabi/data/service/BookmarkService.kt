package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.diary.DiaryDetailResponseDTO
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.data.model.diary.SearchDiaryResponseDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookmarkService {

    @POST("/bookmark/{diaryId}")
    suspend fun addBookmark(
        @Header("Authorization") accessToken: String,
        @Path("diaryId") diaryId: Int
    ): Response<BaseResponse<MessageResponseDTO>>

    @DELETE("/bookmark/{diaryId}")
    suspend fun deleteBookmark(
        @Header("Authorization") accessToken: String,
        @Path("diaryId") diaryId: Int
    ): Response<BaseResponse<MessageResponseDTO>>
}