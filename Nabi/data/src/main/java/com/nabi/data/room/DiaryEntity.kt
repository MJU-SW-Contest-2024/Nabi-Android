package com.nabi.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TempDiary")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    @ColumnInfo
    val diaryTempDate: String,
    @ColumnInfo
    val diaryTempContent: String
)