package com.nabi.domain.repository

import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.model.emotion.AddDiaryEmotionMsg
import com.nabi.domain.model.emotion.EmotionStatistics

interface EmotionRepository {

    suspend fun getDiaryStatistics(
        accessToken: String,
        startDate: String,
        endDate: String
    ): Result<EmotionStatistics>

    suspend fun searchDiaryByEmotion(
        accessToken: String,
        emotion: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<Pair<PageableInfo, List<SearchDiary>>>

    suspend fun getDiaryEmotion(
        accessToken: String,
        diaryId: Int
    ): Result<String>

    suspend fun addDiaryEmotion(
        accessToken: String,
        diaryId: Int,
        emotionState: String
    ): Result<AddDiaryEmotionMsg>

    suspend fun patchDiaryEmotion(
        accessToken: String,
        diaryId: Int,
        emotion: String
    ): Result<String>
}