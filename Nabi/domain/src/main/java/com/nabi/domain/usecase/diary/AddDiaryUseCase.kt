package com.nabi.domain.usecase.diary

import com.nabi.domain.model.diary.AddDiaryInfo
import com.nabi.domain.repository.DiaryRepository

class AddDiaryUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(
        accessToken: String,
        content: String,
        diaryEntryDate: String
    ): Result<AddDiaryInfo> {
        return repository.addDiary("Bearer $accessToken", content, diaryEntryDate)
    }
}