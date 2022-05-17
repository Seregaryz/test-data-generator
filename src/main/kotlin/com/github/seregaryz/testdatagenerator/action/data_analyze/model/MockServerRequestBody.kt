package com.github.seregaryz.testdatagenerator.action.data_analyze.model

import kotlinx.parcelize.Parcelize

@Parcelize
data class MockServerRequestBody(
    val userId: Int? = 1,
    val modelJsonData: String?,
    val internalClassesJsonData: String?,
    val endpoint: String?
)
