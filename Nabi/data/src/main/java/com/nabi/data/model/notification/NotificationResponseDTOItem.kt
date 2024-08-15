package com.nabi.data.model.notification


import com.google.gson.annotations.SerializedName

data class NotificationResponseDTOItem(
    @SerializedName("body")
    val body: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("title")
    val title: String
)