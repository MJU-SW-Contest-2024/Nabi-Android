package com.nabi.domain.repository

import com.nabi.domain.model.diary.DiaryDbEntity

interface DiaryDbRepository {
    suspend fun addTempDiary(diary: DiaryDbEntity): Result<Unit>

    suspend fun updateTempDiary(date: String, content: String): Result<Unit>

    suspend fun getDiaryByDate(date: String): Result<DiaryDbEntity?>
}