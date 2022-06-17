package com.github.seregaryz.testdatagenerator.api

import com.github.seregaryz.testdatagenerator.action.request_history.model.Request
import com.github.seregaryz.testdatagenerator.action.data_analyze.model.MockServerRequestBody
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DataAnalyzeApi {

    @POST("add_new_endpoint")
    fun addNewEndpoint(
        @Body mockServerRequestBody: MockServerRequestBody
    ): Single<String?>

    @GET("admin/endpoint_list")
    fun getEndpointList(
        @Query ("user_id") userId: String?
    ): Single<List<Request?>>

}