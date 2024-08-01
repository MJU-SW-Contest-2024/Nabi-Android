package com.nabi.domain.usecase.diary

import com.nabi.domain.model.diary.MonthDiaryInfo
import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.repository.DiaryRepository

class DiaryUseCase(private val repository: DiaryRepository) {
    suspend operator fun invoke(accessToken: String, year: Int, month: Int): Result<List<MonthDiaryInfo>>{
        return repository.checkMonthDiary("Bearer $accessToken", year, month)
    }
}