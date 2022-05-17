package com.github.seregaryz.testdatagenerator.action.data_analyze.view

import com.github.seregaryz.testdatagenerator.action.data_analyze.injector.DataAnalyzeInjector
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JTextField

class DataAnalyzeForm(
    private val project: Project,
    private val modelJsonData: String?,
    private val internalClassesJsonData: String?,
    private val injector: DataAnalyzeInjector
) : DialogWrapper(project), DataAnalyzeView {

    private var endpointTextField: JTextField? = JTextField().apply {
        name = "endpointTextField"
    }

    private val dataAnalyzePresenter by lazy {
        injector.dataAnalyzePresenter(this)
    }

    init {
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row("Fill the endpoint") {
            endpointTextField?.let { it() }
        }
    }.apply {
        minimumSize = Dimension(360, 120)
        maximumSize = Dimension(360, 120)
    }

    override fun doOKAction() {
        val endpoint = endpointTextField?.text
        if (endpoint?.isNotEmpty() == true) {
            dataAnalyzePresenter.sendParsedData(modelJsonData, internalClassesJsonData, endpoint)
        } else error(Error("Please, fill endpoint field"))
    }

    override fun success(endpoint: String?) {
        close(OK_EXIT_CODE)

        NotificationGroupManager.getInstance()
            .getNotificationGroup("Custom Notification Group")
            .createNotification(
                "Success!", "To get data execute $endpoint", NotificationType.INFORMATION
            ).notify(project)
    }

    override fun error(error: Throwable) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Custom Notification Group")
            .createNotification(
                "Error!", error.localizedMessage, NotificationType.ERROR
            ).notify(project)
    }

}