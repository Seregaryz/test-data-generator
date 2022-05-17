package com.github.seregaryz.testdatagenerator.action.data_analyze.api

import com.github.seregaryz.testdatagenerator.action.data_analyze.model.MockServerRequestBody
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface DataAnalyzeApi {

    @POST("add_new_endpoint")
    fun addNewEndpoint(
        @Body mockServerRequestBody: MockServerRequestBody
    ): Single<String?>

}