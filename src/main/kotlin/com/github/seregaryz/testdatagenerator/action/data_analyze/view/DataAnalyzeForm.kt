package com.github.seregaryz.testdatagenerator.action.data_analyze.view

import com.github.seregaryz.testdatagenerator.action.data_analyze.injector.DataAnalyzeInjector
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JTextField

class DataAnalyzeForm(
    private val project: Project,
    private val modelJsonData: String?,
    private val internalClassesJsonData: String?,
    private val rootElementName: String?,
    private val injector: DataAnalyzeInjector
) : DialogWrapper(project), DataAnalyzeView {

    private var endpointTextField: JTextField? = JTextField().apply {
        name = "endpointTextField"
    }

    private var localeCombo: ComboBox<String>? = ComboBox<String>().apply {
        name = "localeComboBox"
    }

    private var variabilityCombo: ComboBox<String>? = ComboBox<String>().apply {
        name = "variabilityComboBox"
    }

    private var representativenessCombo: ComboBox<String>? = ComboBox<String>().apply {
        name = "representativenessComboBox"
    }

    private val dataAnalyzePresenter by lazy {
        injector.dataAnalyzePresenter(this)
    }

    init {
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row("Endpoint") {
            endpointTextField?.let { it() }
        }
        row("Language") {
            localeCombo?.let { it(grow) }
        }
        row("Variability") {
            variabilityCombo?.let { it(grow) }
        }
        row("Representativeness") {
            representativenessCombo?.let { it(grow) }
        }
    }.apply {
        minimumSize = Dimension(400, 150)
        maximumSize = Dimension(400, 150)
        localeCombo?.addItem("Russian")
        localeCombo?.addItem("English")
        variabilityCombo?.addItem("Static")
        variabilityCombo?.addItem("Dynamic")
        representativenessCombo?.addItem("Representative")
        representativenessCombo?.addItem("Not representative")

    }

    override fun doOKAction() {
        val endpoint = endpointTextField?.text
        if (endpoint?.isNotEmpty() == true) {

            dataAnalyzePresenter.sendParsedData(
                modelJsonData = modelJsonData,
                internalClassesJsonData = internalClassesJsonData,
                method = endpoint,
                language = localeCombo?.selectedItem as String,
                isStatic = (variabilityCombo?.selectedItem as String) == "Static",
                isRepresentative = (representativenessCombo?.selectedItem as String) == "Representative",
                rootElementName = rootElementName
            )
        } else error(Error("Please, fill endpoint field"))
    }

    override fun success(endpointMessage: String?) {
        close(OK_EXIT_CODE)
        endpointMessage?.let { message ->
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Custom Notification Group")
                .createNotification(
                    "Success!", message, NotificationType.INFORMATION
                ).notify(project)
        }
    }

    override fun error(error: Throwable) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Custom Notification Group")
            .createNotification(
                "Error!", error.localizedMessage, NotificationType.ERROR
            ).notify(project)
    }

}