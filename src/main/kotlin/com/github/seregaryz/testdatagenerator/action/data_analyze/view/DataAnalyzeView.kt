package com.github.seregaryz.testdatagenerator.action.data_analyze.view

interface DataAnalyzeView {
    fun success(endpointMessage: String?)
    fun error(error: Throwable)
}