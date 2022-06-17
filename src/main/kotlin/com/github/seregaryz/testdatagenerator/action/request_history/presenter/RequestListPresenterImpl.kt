package com.github.seregaryz.testdatagenerator.action.request_history.presenter

import com.github.seregaryz.testdatagenerator.action.request_history.repository.RequestListRepository
import com.github.seregaryz.testdatagenerator.action.request_history.view.RequestListView

class RequestListPresenterImpl(
    private val view: RequestListView,
    private val repository: RequestListRepository
) : RequestListPresenter {

    override fun getRequestList() {
        repository.getRequestList()
            .subscribe(
                { endpoint ->
                    view.successListLoading(endpoint)
                },
                { error ->
                    view.error(error)
                }
            )
    }



}