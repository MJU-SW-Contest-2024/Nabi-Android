package com.nabi.domain.repository

import com.nabi.domain.model.diary.DiaryInfo

interface DiaryRepository {
    suspend fun getMonthlyDiary(accessToken: String, year: Int, month: Int): Result<List<DiaryInfo>>
}