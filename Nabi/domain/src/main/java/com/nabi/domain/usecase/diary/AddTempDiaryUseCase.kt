package com.nabi.domain.usecase.diary

import com.nabi.domain.model.diary.DiaryDbEntity
import com.nabi.domain.repository.DiaryDbRepository

class AddTempDiaryUseCase(private val repository: DiaryDbRepository) {

    suspend operator fun invoke(
        diaryDbEntity: DiaryDbEntity
    ): Result<Unit> {
        return repository.addTempDiary(diaryDbEntity)
    }
}