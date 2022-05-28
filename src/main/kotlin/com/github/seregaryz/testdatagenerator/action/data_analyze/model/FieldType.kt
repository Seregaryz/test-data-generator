package com.github.seregaryz.testdatagenerator.action.data_analyze.model

import kotlinx.parcelize.Parcelize

@Parcelize
data class FieldType(
    var type: String?,
    var isPrimitive: Boolean? = true
)
