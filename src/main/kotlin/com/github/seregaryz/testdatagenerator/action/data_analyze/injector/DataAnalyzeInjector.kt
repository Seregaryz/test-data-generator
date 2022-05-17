package com.github.seregaryz.testdatagenerator.action.data_analyze.injector

import com.github.seregaryz.testdatagenerator.action.data_analyze.api.DataAnalyzeApi
import com.github.seregaryz.testdatagenerator.action.data_analyze.presenter.DataAnalyzePresenter
import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepository
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeView

interface DataAnalyzeInjector {
    val dataAnalyzeRepository: DataAnalyzeRepository
    val api: DataAnalyzeApi
    fun dataAnalyzePresenter(view: DataAnalyzeView): DataAnalyzePresenter
}