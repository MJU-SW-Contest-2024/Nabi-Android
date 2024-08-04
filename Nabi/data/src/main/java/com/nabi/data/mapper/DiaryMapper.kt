package com.nabi.data.mapper

import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.domain.model.diary.DiaryInfo

object DiaryMapper {

    fun mapperToResponseEntity(items: List<ResponseMonthDiaryDTO>): List<DiaryInfo> {
        return items.map {
            DiaryInfo(
                content = it.content,
                diaryEntryDate = it.diaryEntryDate,
                diaryId = it.diaryId,
                emotion = it.emotion,
                isBookmarked = it.isBookmarked,
                nickname = it.nickname
            )
        }
    }
}