package com.nabi.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTempDiary(diary: DiaryEntity)

    @Query("UPDATE TempDiary SET diaryTempContent = :content WHERE diaryTempDate = :date")
    suspend fun updateTempDiary(date: String, content: String)

    @Query("SELECT * FROM TempDiary WHERE diaryTempDate = :date")
    suspend fun getDiaryByDate(date: String): DiaryEntity?
}