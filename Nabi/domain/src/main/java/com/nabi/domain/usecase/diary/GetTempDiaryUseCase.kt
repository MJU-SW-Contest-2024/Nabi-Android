package com.nabi.domain.usecase.diary

import com.nabi.domain.model.diary.DiaryDbEntity
import com.nabi.domain.repository.DiaryDbRepository

class GetTempDiaryUseCase(private val repository: DiaryDbRepository) {

    suspend operator fun invoke(
        date: String
    ): Result<DiaryDbEntity?> {
        return repository.getDiaryByDate(date)
    }
}