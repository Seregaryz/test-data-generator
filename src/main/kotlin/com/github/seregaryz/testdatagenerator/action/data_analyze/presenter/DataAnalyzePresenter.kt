package com.github.seregaryz.testdatagenerator.action.data_analyze.presenter

interface DataAnalyzePresenter {
    fun sendParsedData(jsonData: String, method: String)
}