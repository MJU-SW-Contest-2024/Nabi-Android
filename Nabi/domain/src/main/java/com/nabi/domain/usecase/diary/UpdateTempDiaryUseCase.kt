package com.nabi.domain.usecase.diary

import com.nabi.domain.repository.DiaryDbRepository

class UpdateTempDiaryUseCase(private val repository: DiaryDbRepository) {

    suspend operator fun invoke(
        date: String,
        content: String
    ): Result<Unit> {
        return repository.updateTempDiary(date, content)
    }
}