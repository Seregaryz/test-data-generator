package com.github.seregaryz.testdatagenerator.action.injector

import com.github.seregaryz.testdatagenerator.action.request_history.presenter.RequestListPresenter
import com.github.seregaryz.testdatagenerator.action.request_history.presenter.RequestListPresenterImpl
import com.github.seregaryz.testdatagenerator.action.request_history.repository.RequestListRepository
import com.github.seregaryz.testdatagenerator.action.request_history.repository.RequestListRepositoryImpl
import com.github.seregaryz.testdatagenerator.action.request_history.view.RequestListView
import com.github.seregaryz.testdatagenerator.api.DataAnalyzeApi
import com.github.seregaryz.testdatagenerator.action.data_analyze.presenter.DataAnalyzePresenter
import com.github.seregaryz.testdatagenerator.action.data_analyze.presenter.DataAnalyzePresenterImpl
import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepository
import com.github.seregaryz.testdatagenerator.action.data_analyze.repository.DataAnalyzeRepositoryImpl
import com.github.seregaryz.testdatagenerator.action.data_analyze.view.DataAnalyzeView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class InjectorImpl : Injector {

    private val okHttpClient: OkHttpClient =
        with(OkHttpClient.Builder()) {
            val httpLogger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addNetworkInterceptor(httpLogger)
            build()
        }

    override val api: DataAnalyzeApi by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://localhost:8080/")
            .build()
            .create(DataAnalyzeApi::class.java)
    }

    override val dataAnalyzeRepository: DataAnalyzeRepository by lazy {
        DataAnalyzeRepositoryImpl(api)
    }

    override fun dataAnalyzePresenter(view: DataAnalyzeView): DataAnalyzePresenter {
        return DataAnalyzePresenterImpl(view, dataAnalyzeRepository)
    }

    override val requestListRepository: RequestListRepository by lazy {
        RequestListRepositoryImpl(api)
    }

    override fun endpointListPresenter(view: RequestListView): RequestListPresenter {
        return RequestListPresenterImpl(view, requestListRepository)
    }
}