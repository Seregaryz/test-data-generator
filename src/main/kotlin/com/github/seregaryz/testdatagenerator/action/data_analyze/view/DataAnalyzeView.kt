package com.github.seregaryz.testdatagenerator.action.data_analyze.view

interface DataAnalyzeView {
    fun onButtonConfirmClick()
    fun success()
    fun error(error: Throwable)
}