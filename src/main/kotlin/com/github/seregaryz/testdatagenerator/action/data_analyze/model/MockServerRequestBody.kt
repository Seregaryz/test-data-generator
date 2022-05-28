package com.github.seregaryz.testdatagenerator.action.data_analyze.model

import kotlinx.parcelize.Parcelize

@Parcelize
data class MockServerRequestBody(
    val userId: String?,
    val rootModel: String?,
    val additionalModels: String?,
    val endpoint: String?,
    val locale: String?,
    val nameOfRootModel: String?,
    val isStatic: Boolean?,
    val isRepresentative: Boolean?
)
