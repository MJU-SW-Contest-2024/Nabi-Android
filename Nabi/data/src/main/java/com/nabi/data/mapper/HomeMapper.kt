package com.nabi.data.mapper

import com.nabi.data.model.home.ResponseHomeDTO
import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.model.home.RecentFiveDiary

object HomeMapper {

    fun mapperToResponseEntity(item: ResponseHomeDTO): HomeInfo {
        return item.run {
            HomeInfo(
                consecutiveWritingDays = this.consecutiveWritingDays,
                nickname = this.nickname,
                recentFiveDiaries = this.recentFiveDiaries.map { diary ->
                    RecentFiveDiary(
                        diaryId = diary.diaryId,
                        content = diary.content,
                        diaryEntryDate = diary.diaryEntryDate,
                        emotion = diary.emotion ?: "",
                        isBookmarked = diary.isBookmarked
                    )
                }
            )
        }
    }
}