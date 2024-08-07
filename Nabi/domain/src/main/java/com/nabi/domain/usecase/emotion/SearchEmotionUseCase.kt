package com.nabi.domain.usecase.emotion

import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.repository.EmotionRepository

class SearchEmotionUseCase(private val repository: EmotionRepository) {
    suspend operator fun invoke(accessToken: String, emotion: String, page: Int, size: Int, sort: String): Result<Pair<PageableInfo, List<SearchDiary>>>{
        return repository.searchDiaryByEmotion("Bearer $accessToken", emotion, page, size, sort)
    }
}