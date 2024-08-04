package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.DiaryRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.diary.DiaryDetailResponseDTO
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.data.model.diary.SearchDiaryResponseDTO
import com.nabi.data.service.DiaryService
import javax.inject.Inject

class DiaryRemoteDataSourceImpl @Inject constructor(
    private val diaryService: DiaryService
) : DiaryRemoteDataSource {
    override suspend fun getMonthlyDiary(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<List<ResponseMonthDiaryDTO>>> {
        return try {
            val response = diaryService.checkMonthDiary(accessToken, year, month)
            if (response.isSuccessful) {
                val diaryResponse = response.body()
                if (diaryResponse != null) {
                    Result.success(diaryResponse)
                } else {
                    Result.failure(Exception("check Month DiaryList fail: response body is null"))
                }
            } else {
                Result.failure(Exception("check Month DiaryList fail: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSearchDiary(
        accessToken: String,
        content: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<BaseResponse<PageableResponse<SearchDiaryResponseDTO>>> {
        return try {
            val response = diaryService.getSearchDiary(accessToken, content, page, size, sort)

            if (response.isSuccessful) {
                val diaryResponse = response.body()
                if (diaryResponse != null) {
                    Result.success(diaryResponse)
                } else {
                    Result.failure(Exception("Search Diary fail: response body is null"))
                }
            } else {
                Result.failure(Exception("Search Diary fail: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDiaryDetail(
        accessToken: String,
        diaryId: Int
    ): Result<BaseResponse<DiaryDetailResponseDTO>> {
        return try {
            val response = diaryService.getDiaryDetail(accessToken, diaryId)

            if (response.isSuccessful) {
                val diaryResponse = response.body()
                if (diaryResponse != null) {
                    Result.success(diaryResponse)
                } else {
                    Result.failure(Exception("Get Diary Detail fail: response body is null"))
                }
            } else {
                Result.failure(Exception("Get Diary Detail fail: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}