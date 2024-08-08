package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.diary.AddDiaryRequestDTO
import com.nabi.data.model.diary.AddDiaryResponseDTO
import com.nabi.data.model.diary.DeleteDiaryResponseDTO
import com.nabi.data.model.diary.DiaryDetailResponseDTO
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.data.model.diary.SearchDiaryResponseDTO
import com.nabi.data.model.diary.UpdateDiaryRequestDTO
import com.nabi.data.model.diary.UpdateDiaryResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DiaryService {

    @GET("/diarys/monthlyDiaryList")
    suspend fun checkMonthDiary(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<List<ResponseMonthDiaryDTO>>>

    @GET("/diarys/contentSearch")
    suspend fun getSearchDiary(
        @Header("Authorization") accessToken: String,
        @Query("content") content: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<BaseResponse<PageableResponse<SearchDiaryResponseDTO>>>

    @GET("/diarys/{diaryId}")
    suspend fun getDiaryDetail(
        @Header("Authorization") accessToken: String,
        @Path("diaryId") diaryId: Int
    ): Response<BaseResponse<DiaryDetailResponseDTO>>

    @POST("/diarys")
    suspend fun addDiary(
        @Header("Authorization") accessToken: String,
        @Body body: AddDiaryRequestDTO
    ): Response<BaseResponse<AddDiaryResponseDTO>>

    @PATCH("/diarys/{id}")
    suspend fun updateDiary(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int,
        @Body body: UpdateDiaryRequestDTO
    ): Response<BaseResponse<UpdateDiaryResponseDTO>>

    @DELETE("/diarys/{id}")
    suspend fun deleteDiary(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int,
    ): Response<BaseResponse<DeleteDiaryResponseDTO>>
}