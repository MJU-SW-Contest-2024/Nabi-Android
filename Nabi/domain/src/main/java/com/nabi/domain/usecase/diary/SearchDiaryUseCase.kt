package com.nabi.domain.usecase.diary

import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.repository.DiaryRepository

class SearchDiaryUseCase(private val repository: DiaryRepository) {

    suspend operator fun invoke(accessToken: String, content: String, page: Int, size: Int, sort: String): Result<Pair<PageableInfo, List<SearchDiary>>>{
        return repository.getSearchDiary("Bearer $accessToken", content, page, size, sort)
    }
}