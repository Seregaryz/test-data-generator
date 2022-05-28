package com.github.seregaryz.testdatagenerator.action.data_analyze.repository

import com.github.seregaryz.testdatagenerator.action.data_analyze.api.DataAnalyzeApi
import com.github.seregaryz.testdatagenerator.action.data_analyze.model.MockServerRequestBody
import com.intellij.internal.statistic.DeviceIdManager
import hu.akarnokd.rxjava2.swing.SwingSchedulers
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class DataAnalyzeRepositoryImpl(
    private val api: DataAnalyzeApi
) : DataAnalyzeRepository {

    override fun sendParsedData(
        modelJsonData: String?,
        internalClassesJsonData: String?,
        method: String,
        language: String,
        isStatic: Boolean,
        isRepresentative: Boolean,
        rootElementName: String?
    ): Single<String?> =
        api.addNewEndpoint(
            MockServerRequestBody(
                userId = DeviceIdManager.getOrGenerateId(null, "UNDEFINED"),
                rootModel = modelJsonData,
                additionalModels = internalClassesJsonData,
                endpoint = method,
                locale = language,
                isStatic = isStatic,
                nameOfRootModel = rootElementName,
                isRepresentative = isRepresentative
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(SwingSchedulers.edt())
}