package com.nabi.domain.usecase.emotion

import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.model.emotion.AddDiaryEmotionMsg
import com.nabi.domain.model.emotion.EmotionStatistics
import com.nabi.domain.repository.EmotionRepository

class AddDiaryEmotionUseCase(private val repository: EmotionRepository) {
    suspend operator fun invoke(
        accessToken: String,
        diaryId: Int,
        emotionState: String
    ): Result<AddDiaryEmotionMsg> {
        return repository.addDiaryEmotion("Bearer $accessToken", diaryId, emotionState)
    }
}