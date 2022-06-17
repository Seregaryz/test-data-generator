package com.github.seregaryz.testdatagenerator.action.data_analyze.view

import com.github.seregaryz.testdatagenerator.action.injector.Injector
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiElement
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.layout.panel
import java.awt.Dimension
import java.text.NumberFormat
import javax.swing.*
import javax.swing.text.NumberFormatter


class DataAnalyzeViewImpl(
    private val project: Project,
    private val selectedElement: PsiElement,
    private val injector: Injector
) : DialogWrapper(project), DataAnalyzeView {

    private var isListSelected = false

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

    private var listLabel: JLabel = JLabel("List size").apply {
        name = "list label"
    }

    private var listCountTextField: JTextField? = JTextField().apply {
        name = "list size"
    }

    private var singleElementRadioButton: JBRadioButton = JBRadioButton("Single element", true)
    private var listRadioButton: JBRadioButton = JBRadioButton("List", false)

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
        row {
            singleElementRadioButton()
            listRadioButton()
        }
        row {
            listLabel()
            listCountTextField?.let {
                it()
            }
        }
    }.apply {
        listLabel.isVisible = false
        listCountTextField?.isVisible = false
        minimumSize = Dimension(400, 250)
        maximumSize = Dimension(400, 250)
        localeCombo?.addItem("Russian")
        localeCombo?.addItem("English")
        variabilityCombo?.addItem("Static")
        variabilityCombo?.addItem("Dynamic")
        representativenessCombo?.addItem("Representative")
        representativenessCombo?.addItem("Not representative")
        val group = ButtonGroup()
        group.add(singleElementRadioButton)
        group.add(listRadioButton)
        singleElementRadioButton.addItemListener {
            listLabel.isVisible = false
            listCountTextField?.isVisible = false
            isListSelected = false
        }
        listRadioButton.addItemListener {
            listLabel.isVisible = true
            listCountTextField?.isVisible = true
            isListSelected = true
        }
    }

    override fun doOKAction() {
        val endpoint = endpointTextField?.text
        val isCountValid = if (isListSelected) {
            try {
                Integer.parseInt(listCountTextField?.text)
                true
            } catch(e: NumberFormatException) {
                false
            }
        } else true
        if (endpoint?.isNotEmpty() == true && isCountValid) {
            val count = if (isListSelected) Integer.parseInt(listCountTextField?.text) else 0
            dataAnalyzePresenter.analyzeModelData(
                project = project,
                selectedElement = selectedElement,
                method = endpoint,
                language = localeCombo?.selectedItem as String,
                isStatic = (variabilityCombo?.selectedItem as String) == "Static",
                isRepresentative = (representativenessCombo?.selectedItem as String) == "Representative",
                isList = isListSelected,
                elementsCount = count
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