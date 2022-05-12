package com.github.seregaryz.testdatagenerator.action.data_analyze

import com.github.seregaryz.testdatagenerator.action.data_analyze.presenter.DataAnalyzePresenter
import com.github.seregaryz.testdatagenerator.action.data_analyze.presenter.DataAnalyzePresenterImpl
import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepository
import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepositoryImpl
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeView

interface DataAnalyzeInjector {
    val dataAnalyzeRepository: DataAnalyzeRepository

    fun trelloActionPresenter(view: DataAnalyzeView): DataAnalyzePresenter
}

class DataAnalyzeInjectorImpl: DataAnalyzeInjector {

    override val dataAnalyzeRepository: DataAnalyzeRepository by lazy {
        DataAnalyzeRepositoryImpl()
    }

    override fun trelloActionPresenter(view: DataAnalyzeView): DataAnalyzePresenter {
        return DataAnalyzePresenterImpl(view, dataAnalyzeRepository)
    }
}