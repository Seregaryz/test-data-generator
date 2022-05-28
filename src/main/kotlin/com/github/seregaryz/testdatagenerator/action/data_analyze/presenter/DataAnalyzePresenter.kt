package com.github.seregaryz.testdatagenerator.action.data_analyze.presenter

interface DataAnalyzePresenter {
    fun sendParsedData(
        modelJsonData: String?,
        internalClassesJsonData: String?,
        method: String,
        language: String,
        isStatic: Boolean,
        isRepresentative: Boolean,
        rootElementName: String?
    )
}