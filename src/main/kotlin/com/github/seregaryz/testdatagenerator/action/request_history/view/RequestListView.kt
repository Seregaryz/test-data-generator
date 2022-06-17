package com.github.seregaryz.testdatagenerator.action.request_history.view

import com.github.seregaryz.testdatagenerator.action.request_history.model.Request

interface RequestListView {
    fun successListLoading(endpointsList: List<Request?>?)
    fun error(error: Throwable)
}