package com.nabi.data.repository

import com.nabi.data.room.DiaryDAO
import com.nabi.data.room.DiaryEntity
import com.nabi.domain.model.diary.DiaryDbEntity
import com.nabi.domain.repository.DiaryDbRepository
import javax.inject.Inject

class DiaryDbRepositoryImpl @Inject constructor(
    private val diaryDao: DiaryDAO
) : DiaryDbRepository {
    override suspend fun addTempDiary(diary: DiaryDbEntity): Result<Unit> {
        return try {
            diaryDao.addTempDiary(
                DiaryEntity(
                    diaryTempDate = diary.diaryTempDate,
                    diaryTempContent = diary.diaryTempContent
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDiaryByDate(date: String): Result<DiaryDbEntity?> {
        return try {
            val diaryEntity = diaryDao.getDiaryByDate(date)
            if (diaryEntity != null) {
                Result.success(
                    DiaryDbEntity(
                        diaryTempDate = diaryEntity.diaryTempDate,
                        diaryTempContent = diaryEntity.diaryTempContent
                    )
                )
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTempDiary(date: String, content: String): Result<Unit> {
        return try {
            diaryDao.updateTempDiary(date, content)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}