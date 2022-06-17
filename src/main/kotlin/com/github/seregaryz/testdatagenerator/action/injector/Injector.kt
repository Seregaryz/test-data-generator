package com.github.seregaryz.testdatagenerator.action.injector

import com.github.seregaryz.testdatagenerator.action.request_history.presenter.RequestListPresenter
import com.github.seregaryz.testdatagenerator.action.request_history.repository.RequestListRepository
import com.github.seregaryz.testdatagenerator.action.request_history.view.RequestListView
import com.github.seregaryz.testdatagenerator.api.DataAnalyzeApi
import com.github.seregaryz.testdatagenerator.action.data_analyze.presenter.DataAnalyzePresenter
import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepository
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeView

interface Injector {

    val dataAnalyzeRepository: DataAnalyzeRepository
    val api: DataAnalyzeApi
    fun dataAnalyzePresenter(view: DataAnalyzeView): DataAnalyzePresenter
    val requestListRepository: RequestListRepository
    fun endpointListPresenter(view: RequestListView): RequestListPresenter

}