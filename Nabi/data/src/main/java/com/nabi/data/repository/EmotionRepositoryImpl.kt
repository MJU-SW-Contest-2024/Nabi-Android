package com.nabi.data.repository

import com.nabi.data.datasource.EmotionRemoteDataSource
import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.model.emotion.AddDiaryEmotionMsg
import com.nabi.domain.model.emotion.EmotionStatistics
import com.nabi.domain.repository.EmotionRepository
import javax.inject.Inject

class EmotionRepositoryImpl @Inject constructor(
    private val emotionRemoteDataSource: EmotionRemoteDataSource
) : EmotionRepository {

    override suspend fun getDiaryStatistics(
        accessToken: String,
        startDate: String,
        endDate: String
    ): Result<EmotionStatistics> {
        val result = emotionRemoteDataSource.getDiaryStatistics(accessToken, startDate, endDate)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    val signInInfo = data.run {
                        EmotionStatistics(
                            angerCount,
                            anxietyCount,
                            depressionCount,
                            happinessCount,
                            boringCount
                        )
                    }
                    Result.success(signInInfo)
                } else {
                    Result.failure(Exception("Get Emotion Statistics Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Get Emotion Statistics Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun searchDiaryByEmotion(
        accessToken: String,
        emotion: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<Pair<PageableInfo, List<SearchDiary>>> {
        val result =
            emotionRemoteDataSource.searchDiaryByEmotion(accessToken, emotion, page, size, sort)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    val pageableInfo = PageableInfo(
                        totalPages = data.totalPages,
                        totalElements = data.totalElements,
                        elementSize = data.size,
                        currentPageNumber = data.number,
                        isLastPage = data.last
                    )

                    if (data.size == 0) {
                        Result.success(Pair(pageableInfo, emptyList()))
                    } else {
                        val searchDiaryList = data.content.map {
                            SearchDiary(
                                it.content,
                                it.diaryEntryDate,
                                it.diaryId
                            )
                        }
                        Result.success(Pair(pageableInfo, searchDiaryList))
                    }
                } else {
                    Result.failure(Exception("Search Emotion Data is null"))
                }
            } else {
                Result.failure(Exception("Search Emotion Data Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getDiaryEmotion(accessToken: String, diaryId: Int): Result<String> {
        val result = emotionRemoteDataSource.getDiaryEmotion(accessToken, diaryId)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(data)
                } else {
                    Result.failure(Exception("Get Diary Emotion Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Get Diary Emotion Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun addDiaryEmotion(
        accessToken: String,
        diaryId: Int,
        emotionState: String
    ): Result<AddDiaryEmotionMsg> {
        val result = emotionRemoteDataSource.addDiaryEmotion(accessToken, diaryId, emotionState)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    val msg = AddDiaryEmotionMsg(data.message)
                    Result.success(msg)
                } else {
                    Result.failure(Exception("Add Diary Emotion Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Add Diary Emotion Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}