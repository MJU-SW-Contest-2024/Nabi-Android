package com.nabi.domain.repository

import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.diary.AddDiaryInfo
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.model.diary.UpdateDiaryInfo

interface DiaryRepository {
    suspend fun getMonthlyDiary(accessToken: String, year: Int, month: Int): Result<List<DiaryInfo>>

    suspend fun getSearchDiary(
        accessToken: String,
        content: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<Pair<PageableInfo, List<SearchDiary>>>

    suspend fun getDiaryDetail(accessToken: String, diaryId: Int): Result<DiaryInfo>

    suspend fun addDiary(
        accessToken: String,
        content: String,
        diaryEntryDate: String
    ): Result<AddDiaryInfo>

    suspend fun updateDiary(
        accessToken: String,
        id: Int,
        content: String
    ): Result<UpdateDiaryInfo>
}