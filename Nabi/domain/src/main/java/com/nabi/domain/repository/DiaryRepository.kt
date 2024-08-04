package com.nabi.domain.repository

import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.model.diary.SearchDiary

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
}