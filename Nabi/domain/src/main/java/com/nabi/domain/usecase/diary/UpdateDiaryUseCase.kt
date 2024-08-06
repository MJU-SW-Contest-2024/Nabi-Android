package com.nabi.domain.usecase.diary

import com.nabi.domain.model.diary.UpdateDiaryInfo
import com.nabi.domain.repository.DiaryRepository

class UpdateDiaryUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(
        accessToken: String,
        id: Int,
        content: String,
        diaryEntryDate: String
    ): Result<UpdateDiaryInfo> {
        return repository.updateDiary("Bearer $accessToken", id, content, diaryEntryDate)
    }
}