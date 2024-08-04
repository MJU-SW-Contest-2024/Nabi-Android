package com.nabi.domain.usecase.bookmark

import com.nabi.domain.repository.BookmarkRepository

class DeleteBookmarkUseCase(private val repository: BookmarkRepository) {

    suspend operator fun invoke(accessToken: String, diaryId: Int): Result<String>{
        return repository.deleteBookmark("Bearer $accessToken", diaryId)
    }
}