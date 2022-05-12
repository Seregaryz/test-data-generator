package com.github.seregaryz.testdatagenerator.action.data_analyze.presenter

import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepository
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeView

class DataAnalyzePresenterImpl(
    private val view: DataAnalyzeView,
    private val repository: DataAnalyzeRepository
): DataAnalyzePresenter {

    override fun sendParsedData(jsonData: String, method: String) {
        repository.sendParsedData(jsonData, method)
    }
}