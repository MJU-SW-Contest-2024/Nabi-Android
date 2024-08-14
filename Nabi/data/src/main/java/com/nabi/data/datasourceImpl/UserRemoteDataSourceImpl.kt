package com.nabi.data.datasourceImpl

import android.util.Log
import com.nabi.data.datasource.UserRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.user.UserInfoResponseDTO
import com.nabi.data.service.UserService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {

    override suspend fun getUserInfo(
        accessToken: String,
    ): Result<BaseResponse<UserInfoResponseDTO>> {
        return try {
            val response = userService.getUserInfo(accessToken)
            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse != null) {
                    Result.success(userResponse)
                } else {
                    Result.failure(Exception("Get User Info failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Get User Info failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadDiary(
        accessToken: String,
        realPath: String
    ): Result<BaseResponse<MessageResponseDTO>> {
        return try {
            val file = File(realPath)
            val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val res = userService.loadDiary(accessToken, body)
            if (res.isSuccessful) Result.success(res.body()!!)
            else {
                val errorBody = JSONObject(res.errorBody()?.string()!!)
                Result.failure(Exception("Load Diary failed: ${errorBody.getString("message")}"))
            }

        } catch (e: Exception) {
            Log.e("TAG", e.stackTraceToString())
            Result.failure(e)
        }
    }
}