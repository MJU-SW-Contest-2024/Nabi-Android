package com.nabi.domain.usecase.diary

import com.nabi.domain.model.diary.DeleteDiaryMsg
import com.nabi.domain.model.diary.UpdateDiaryInfo
import com.nabi.domain.repository.DiaryRepository

class DeleteDiaryUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(
        accessToken: String,
        id: Int,
    ): Result<DeleteDiaryMsg> {
        return repository.deleteDiary("Bearer $accessToken", id)
    }
}