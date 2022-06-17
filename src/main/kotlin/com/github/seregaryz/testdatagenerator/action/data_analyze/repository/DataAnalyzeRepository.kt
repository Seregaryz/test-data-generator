package com.github.seregaryz.testdatagenerator.action.data_analyze.repository

import io.reactivex.Single

interface DataAnalyzeRepository {
    fun sendParsedData(
        modelJsonData: String?,
        internalClassesJsonData: String?,
        method: String,
        language: String,
        isStatic: Boolean,
        isRepresentative: Boolean,
        rootElementName: String?,
        isList: Boolean?,
        elementsCount: Int?
    ): Single<String?>
}