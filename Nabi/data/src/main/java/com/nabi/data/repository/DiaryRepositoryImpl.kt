package com.nabi.data.repository

import com.nabi.data.datasource.DiaryRemoteDataSource
import com.nabi.data.mapper.DiaryMapper
import com.nabi.data.model.diary.AddDiaryRequestDTO
import com.nabi.data.model.diary.UpdateDiaryRequestDTO
import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.diary.AddDiaryInfo
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.model.diary.UpdateDiaryInfo
import com.nabi.domain.repository.DiaryRepository
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource
) : DiaryRepository {

    override suspend fun getMonthlyDiary(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<DiaryInfo>> {
        val result = diaryRemoteDataSource.getMonthlyDiary(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(DiaryMapper.mapperToResponseEntity(data))
                } else {
                    Result.failure(Exception("Month diary List data is null"))
                }
            } else {
                Result.failure(Exception("Month Diary List Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getSearchDiary(
        accessToken: String,
        content: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<Pair<PageableInfo, List<SearchDiary>>> {
        val result = diaryRemoteDataSource.getSearchDiary(accessToken, content, page, size, sort)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    val pageableInfo = PageableInfo(
                        totalPages = data.totalPages,
                        totalElements = data.totalElements,
                        elementSize = data.size,
                        currentPageNumber = data.number,
                        isLastPage = data.last
                    )

                    if (data.size == 0) {
                        Result.success(Pair(pageableInfo, emptyList()))
                    } else {
                        val searchDiaryList = data.content.map {
                            SearchDiary(
                                it.previewContent,
                                it.diaryEntryDate,
                                it.diaryId
                            )
                        }
                        Result.success(Pair(pageableInfo, searchDiaryList))
                    }
                } else {
                    Result.failure(Exception("Search Diary Data is null"))
                }
            } else {
                Result.failure(Exception("Search Diary Data Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getDiaryDetail(accessToken: String, diaryId: Int): Result<DiaryInfo> {
        val result = diaryRemoteDataSource.getDiaryDetail(accessToken, diaryId)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(
                        DiaryInfo(
                            content = data.content,
                            diaryId = data.diaryId,
                            nickname = data.nickname,
                            emotion = data.emotion,
                            diaryEntryDate = data.diaryEntryDate,
                            isBookmarked = data.isBookmarked
                        )
                    )
                } else {
                    Result.failure(Exception("Month diary List data is null"))
                }
            } else {
                Result.failure(Exception("Month Diary List Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun addDiary(
        accessToken: String,
        content: String,
        diaryEntryDate: String
    ): Result<AddDiaryInfo> {
        val result =
            diaryRemoteDataSource.addDiary(accessToken, AddDiaryRequestDTO(content, diaryEntryDate))

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    val addDiaryInfo = AddDiaryInfo(
                        content = data.content,
                        diaryEntryDate = data.diaryEntryDate
                    )
                    Result.success(addDiaryInfo)
                } else {
                    Result.failure(Exception("Add Diary Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Add Diary Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun updateDiary(
        accessToken: String,
        id: Int,
        content: String
    ): Result<UpdateDiaryInfo> {
        val result =
            diaryRemoteDataSource.updateDiary(accessToken, id, UpdateDiaryRequestDTO(content))

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    val updateDiaryInfo = UpdateDiaryInfo(
                        diaryId = data.diaryId,
                        content = data.content,
                        diaryEntryDate = data.diaryEntryDate
                    )
                    Result.success(updateDiaryInfo)
                } else {
                    Result.failure(Exception("Update Diary Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Update Diary Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

}