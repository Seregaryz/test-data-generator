package com.github.seregaryz.testdatagenerator.action.request_history.repository

import com.github.seregaryz.testdatagenerator.action.request_history.model.Request
import io.reactivex.Single

interface RequestListRepository {

    fun getRequestList(): Single<List<Request?>>

}