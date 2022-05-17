package com.github.seregaryz.testdatagenerator.action.data_analyze.repository

import com.github.seregaryz.testdatagenerator.action.data_analyze.api.DataAnalyzeApi
import com.github.seregaryz.testdatagenerator.action.data_analyze.model.MockServerRequestBody
import hu.akarnokd.rxjava2.swing.SwingSchedulers
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class DataAnalyzeRepositoryImpl(
    private val api: DataAnalyzeApi
) : DataAnalyzeRepository {

    override fun sendParsedData(
        modelJsonData: String?,
        internalClassesJsonData: String?,
        method: String
    ): Single<String?> =
        api.addNewEndpoint(
            MockServerRequestBody(
                modelJsonData = modelJsonData,
                internalClassesJsonData = internalClassesJsonData,
                endpoint = method
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(SwingSchedulers.edt())
}