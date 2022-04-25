package com.github.seregaryz.testdatagenerator.action.data_analyze.repository

interface DataAnalyzeRepository {
    fun sendParsedData(jsonData: String, method: String)
}