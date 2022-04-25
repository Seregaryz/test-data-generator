package com.github.seregaryz.testdatagenerator.action.data_analyze.presenter

import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepository

class DataAnalyzePresenterImpl(
    private val repository: DataAnalyzeRepository
): DataAnalyzePresenter {

    override fun sendParsedData(jsonData: String, method: String) {
        repository.sendParsedData(jsonData, method)
    }
}