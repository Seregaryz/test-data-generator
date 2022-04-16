package org.example.data_generator.actions.data_analyze.presenter

import org.example.data_generator.actions.data_analyze.repository.DataAnalyzeRepository
import org.example.data_generator.actions.data_analyze.view.DataAnalyzeSettingsView

class DataAnalyzePresenterImpl(
    private val view: DataAnalyzeSettingsView,
    private val repository: DataAnalyzeRepository
): DataAnalyzePresenter {

    override fun analyzeData() {

    }
}