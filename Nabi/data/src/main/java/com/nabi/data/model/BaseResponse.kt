package com.nabi.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data")
    val data: T?,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)
