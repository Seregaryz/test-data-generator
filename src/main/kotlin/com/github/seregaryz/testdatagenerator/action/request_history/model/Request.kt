package com.github.seregaryz.testdatagenerator.action.request_history.model

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Request(

    @SerializedName("id")
    val id: Long?,

    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("path")
    val endpointPath: String?,

    @SerializedName("sendDate")
    val sendDate: String?,

    @SerializedName("data")
    val data: String?


) {
    override fun toString(): String {
        return "endpoint: /$endpointPath, sendTime=$sendDate"
    }
}
