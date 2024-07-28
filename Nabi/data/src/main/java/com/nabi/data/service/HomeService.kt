package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.home.ResponseHomeDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeService {
    @GET("/home")
    suspend fun fetchHomeData(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseHomeDTO>>
}