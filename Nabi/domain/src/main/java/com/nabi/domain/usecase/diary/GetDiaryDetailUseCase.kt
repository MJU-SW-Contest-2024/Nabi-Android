package com.nabi.domain.usecase.diary

import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.repository.DiaryRepository

class GetDiaryDetailUseCase(private val repository: DiaryRepository) {

    suspend operator fun invoke(accessToken: String, diaryId: Int): Result<DiaryInfo>{
        return repository.getDiaryDetail("Bearer $accessToken", diaryId)
    }
}