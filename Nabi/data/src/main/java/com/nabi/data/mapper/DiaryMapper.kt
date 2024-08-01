package com.nabi.data.mapper

import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.data.model.home.ResponseHomeDTO
import com.nabi.domain.model.diary.MonthDiaryInfo
import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.model.home.RecentFiveDiary

object DiaryMapper {
    fun mapperToResponseEntity(items: List<ResponseMonthDiaryDTO>): List<MonthDiaryInfo> {
        val diaryList = mutableListOf<MonthDiaryInfo>()

        for (item in items) {
            val day = item.diaryEntryDate.split("-").last()

            diaryList.add(
                MonthDiaryInfo(
                    diaryEntryDate = day
                )
            )
        }

        return diaryList
    }
}