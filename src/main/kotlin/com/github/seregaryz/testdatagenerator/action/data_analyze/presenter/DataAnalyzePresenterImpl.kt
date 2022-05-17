package com.github.seregaryz.testdatagenerator.action.data_analyze.presenter

import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepository
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeView

class DataAnalyzePresenterImpl(
    private val view: DataAnalyzeView,
    private val repository: DataAnalyzeRepository
): DataAnalyzePresenter {

    override fun sendParsedData(modelJsonData: String?, internalClassesJsonData: String?, method: String) {
        repository.sendParsedData(modelJsonData, internalClassesJsonData, method)
            .subscribe(
                { endpoint -> view.success(endpoint) },
                { error -> view.error(error) }
            )
    }
}