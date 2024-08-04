package com.nabi.domain.usecase.bookmark

import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.repository.BookmarkRepository

class AddBookmarkUseCase(private val repository: BookmarkRepository) {

    suspend operator fun invoke(accessToken: String, diaryId: Int): Result<String>{
        return repository.addBookmark("Bearer $accessToken", diaryId)
    }
}