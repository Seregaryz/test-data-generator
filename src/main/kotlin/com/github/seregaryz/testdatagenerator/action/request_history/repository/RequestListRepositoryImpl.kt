package com.github.seregaryz.testdatagenerator.action.request_history.repository

import com.github.seregaryz.testdatagenerator.action.request_history.model.Request
import com.github.seregaryz.testdatagenerator.api.DataAnalyzeApi
import com.intellij.internal.statistic.DeviceIdManager
import hu.akarnokd.rxjava2.swing.SwingSchedulers
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RequestListRepositoryImpl(
    private val api: DataAnalyzeApi
) : RequestListRepository {

    override fun getRequestList(): Single<List<Request?>> =
        api.getEndpointList(userId = DeviceIdManager.getOrGenerateId(null, "UNDEFINED"))
            .subscribeOn(Schedulers.io())
            .observeOn(SwingSchedulers.edt())

}