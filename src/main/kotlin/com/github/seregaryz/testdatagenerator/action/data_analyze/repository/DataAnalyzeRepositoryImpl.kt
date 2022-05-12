package com.github.seregaryz.testdatagenerator.action.data_analyze.repository

import com.github.seregaryz.testdatagenerator.action.data_analyze.model.MockServerRequestBody
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class DataAnalyzeRepositoryImpl : DataAnalyzeRepository {


    private val url = "https://localhost:8080/add_new_endpoint"

    companion object {
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }

    override fun sendParsedData(jsonData: String, method: String) {
        //Отправка данных на mock-сервер
        val client = OkHttpClient()
        val requestBody = MockServerRequestBody(jsonData, method)
        val request: Request =
            Request.Builder().url(url).post(Gson().toJson(requestBody).toRequestBody(MEDIA_TYPE_JSON)).build()
        client.newCall(request).execute().use { response ->
            val responseBody: String? = response.body?.string()
        }
    }
}