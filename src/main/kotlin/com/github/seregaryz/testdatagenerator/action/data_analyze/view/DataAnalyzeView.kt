package com.github.seregaryz.testdatagenerator.action.data_analyze.view

interface DataAnalyzeView {
    fun success(endpoint: String?)
    fun error(error: Throwable)
}