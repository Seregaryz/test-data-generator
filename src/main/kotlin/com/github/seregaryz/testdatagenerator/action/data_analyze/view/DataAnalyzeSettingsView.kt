package org.example.data_generator.actions.data_analyze.view

interface DataAnalyzeSettingsView {
    fun showStartSetting()
    fun success()
    fun error(error: Throwable)
}