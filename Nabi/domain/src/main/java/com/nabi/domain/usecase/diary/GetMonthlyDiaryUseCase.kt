package com.nabi.domain.usecase.diary

import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.repository.DiaryRepository

class GetMonthlyDiaryUseCase(private val repository: DiaryRepository) {

    suspend operator fun invoke(accessToken: String, year: Int, month: Int): Result<List<DiaryInfo>>{
        return repository.getMonthlyDiary("Bearer $accessToken", year, month)
    }
}