package com.nabi.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTempDiary(diary: DiaryEntity): Long

    @Update
    suspend fun updateTempDiary(diary: DiaryEntity)

    @Query("SELECT * FROM TempDiary WHERE diaryTempDate = :date LIMIT 1")
    suspend fun getDiaryByDate(date: String): DiaryEntity?
}