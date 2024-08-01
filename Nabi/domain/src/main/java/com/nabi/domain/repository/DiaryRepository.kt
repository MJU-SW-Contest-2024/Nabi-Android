package com.nabi.domain.repository

import com.nabi.domain.model.diary.MonthDiaryInfo

interface DiaryRepository {
    suspend fun checkMonthDiary(accessToken: String, year: Int, month: Int): Result<List<MonthDiaryInfo>>
}